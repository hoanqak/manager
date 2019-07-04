package com.manager.service.Impl;

import com.manager.dto.CheckInOutDTO;
import com.manager.model.CheckInOut;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.CheckInOutRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.CheckInOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CheckInOutServiceImpl implements CheckInOutService {

	@Autowired
	CheckInOutRepository checkInOutRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;

	public boolean compareDate(Date date, Date date1) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (date != null && date1 != null) {
			String compare1 = simpleDateFormat.format(date);
			String compare2 = simpleDateFormat.format(date1);
			return compare1.equals(compare2);
		}
		return false;

	}

	@Override
	public ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		String code = request.getHeader("access_Token");
		if (code != null) {
			Token token = tokenRepository.getTokenByCode(code);
			User user = userRepository.getUserById(token.getId());
			System.out.println(user);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(checkInOutDTO.getTimeCheck());
			CheckInOut checkInOut = new CheckInOut();
			int hourCheckIn = calendar.get(Calendar.HOUR_OF_DAY);
			int minuteCheckIn = calendar.get(Calendar.MINUTE);
			System.out.println("gio: " + hourCheckIn);
			System.out.println("Phut: " + minuteCheckIn);

			Date date = checkInOutRepository.getDate(user.getId());

			Date dateNow = new Date();

			if (compareDate(date, dateNow)) {
				return new ResponseEntity<>("CHECKED", HttpStatus.OK);
			} else {
				if ((hourCheckIn == 8 && minuteCheckIn < 30) || (hourCheckIn < 8 && minuteCheckIn < 59)) {
					return new ResponseEntity<>("CHECKIN_FAILED_BEFORE_8h30", HttpStatus.OK);
				} else if (hourCheckIn >= 8 && minuteCheckIn >= 30 && hourCheckIn <= 9) {
					calendar.set(Calendar.HOUR_OF_DAY, 9);
					calendar.set(Calendar.MINUTE, 0);
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setUser(user);
					checkInOutRepository.save(checkInOut);
					return new ResponseEntity<>("CHECKIN_SUCCESS", HttpStatus.OK);
				} else if (hourCheckIn >= 9 && hourCheckIn <= 10 && minuteCheckIn <= 30) {
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setUser(user);

					checkInOutRepository.save(checkInOut);
					return new ResponseEntity<>("CHECKIN_SUCCESS", HttpStatus.OK);
				} else if (hourCheckIn >= 10 && minuteCheckIn > 30 && hourCheckIn < 11) {
					calendar.set(Calendar.HOUR_OF_DAY, 13);
					calendar.set(Calendar.MINUTE, 0);
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setUser(user);
					checkInOutRepository.save(checkInOut);
					//  return new ResponseEntity<>("CHECKIN_FAILED_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<>("CHECKIN_FAILED_AFTER_10h30", HttpStatus.OK);
				} else if (hourCheckIn == 12 && minuteCheckIn <= 59) {
					checkInOut.setUser(user);
					checkInOut.setDayCheckIn(calendar.getTime());
					calendar.set(Calendar.HOUR_OF_DAY, 13);
					calendar.set(Calendar.MINUTE, 0);
					checkInOut.setStartTime(calendar.getTime());
					checkInOutRepository.save(checkInOut);
					//return new ResponseEntity<String>("CHECKIN_SUCCESS_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<String>("CHECKIN_SUCCESS", HttpStatus.OK);
				} else if (hourCheckIn >= 13 && hourCheckIn <= 16) {
					if (hourCheckIn == 16 && minuteCheckIn > 1) {
						//return new ResponseEntity<>("CHECKIN_FAILED_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
						return new ResponseEntity<>("CHECKIN_FAILED_AFTER_16h", HttpStatus.OK);
					}
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setUser(user);
					checkInOutRepository.save(checkInOut);
					//return new ResponseEntity<>("CHECKIN_SUCCESS_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<>("CHECKIN_SUCCESS", HttpStatus.OK);

				} else {
					// return new ResponseEntity<>("TIMEOUT" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<>("TIMEOUT", HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);

	}

	@Override
	public ResponseEntity checkOut(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		String codeToken = request.getHeader("access_Token");
		if (codeToken == null) {
			return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);
		}
		Token token = tokenRepository.getTokenByCode(codeToken);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dateNow = Calendar.getInstance();
		String dateSearch = simpleDateFormat.format(dateNow.getTime());
		int totalMillis = 0;
		//search today\
		//tim ngay checkin moi nhat
		CheckInOut checkInOut = checkInOutRepository.getCheckInOutByDate(dateSearch, token.getId());
		if (checkInOut != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(checkInOutDTO.getTimeCheck());
			if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
				calendar.set(Calendar.HOUR_OF_DAY, 12);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				totalMillis = 60;
			} else if (calendar.get(Calendar.HOUR_OF_DAY) >= 18) {
				calendar.set(Calendar.HOUR_OF_DAY, 18);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
			}
            /*System.out.print(calendar.get(Calendar.HOUR_OF_DAY) + "h");
            System.out.print(calendar.get(Calendar.MINUTE) + "m");
            System.out.print(calendar.get(Calendar.SECOND) + "s");*/
			long timeCheckIn = checkInOut.getStartTime().getTime();
			long timeCheckOut = calendar.getTimeInMillis();

/*
            System.out.println("Last time: " + timeCheckOut);
*/
			totalMillis += (int) (timeCheckOut - timeCheckIn) / 1000;
			/*System.out.println("time working: " + (((totalMillis + 1) /60) - 60));*/
			int totalTime = (totalMillis / 60) - 60;
           /* System.out.println("Total Millis: " + totalMillis);
            int h = totalMillis / 3600;
            int m = totalMillis % 3600 / 60;
            int s = totalMillis % 3600 % 60;
            System.out.println("h: " + h +" m: " + m + " s: " + s);
*/
			if (timeCheckOut < timeCheckIn) {
				totalTime = 0;
			}
			checkInOut.setEndTime(calendar.getTime());
			checkInOut.setTotalTime(totalTime);

			checkInOutRepository.save(checkInOut);

			return new ResponseEntity("CHECKOUT_SUCCESS", HttpStatus.OK);

		} else {
			return new ResponseEntity<>("PLEASE_CHECKIN", HttpStatus.OK);
		}


	}

	@Override
	public ResponseEntity<List<CheckInOut>> getListCheckInOut(HttpServletRequest request) {
		String code = request.getHeader("access_Token");
		Token token = tokenRepository.getTokenByCode(code);
		int userId = token.getId();
		List<CheckInOut> checkInOutList = checkInOutRepository.getListCheckInOutByIdUser(userId);
		return new ResponseEntity<List<CheckInOut>>(checkInOutList, HttpStatus.OK);
	}
	@SuppressWarnings("Duplicates")
	@Override
	public ResponseEntity pageGetAllCheckInsAllUserByDate(long date, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<CheckInOut> page = checkInOutRepository.findCheckInOutsByDayCheckIn(pageable, new Date(date));
		List<CheckInOut> checkInOuts = page.getContent();
		List<CheckInOutDTO> checkInOutDTOS = new ArrayList<>();
		for (CheckInOut checkInOut : checkInOuts) {
			checkInOutDTOS.add(new CheckInOutDTO(checkInOut));
		}
		return new ResponseEntity(checkInOutDTOS, HttpStatus.OK);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<CheckInOut> page = checkInOutRepository.findCheckInOutsByDayCheckInAndAndUserId(pageable, new Date(startDate), new Date(endDate), idUser);
		List<CheckInOut> checkInOuts = page.getContent();
		List<CheckInOutDTO> checkInOutDTOS = new ArrayList<>();
		for (CheckInOut checkInOut : checkInOuts) {
			checkInOutDTOS.add(new CheckInOutDTO(checkInOut));
		}
		return new ResponseEntity(checkInOutDTOS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity getACheckInById(int id) {

		return new ResponseEntity(new CheckInOutDTO(checkInOutRepository.getOne(id)), HttpStatus.OK);

	}
}
