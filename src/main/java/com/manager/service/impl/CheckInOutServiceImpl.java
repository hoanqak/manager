package com.manager.service.impl;

import com.manager.data.Notifications;
import com.manager.data.Position;
import com.manager.dto.CheckInOutDTO;
import com.manager.dto.Checkin2Admin;
import com.manager.dto.PagedResponse;
import com.manager.model.CheckInOut;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.CheckInOutRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.CheckInOutService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CheckInOutServiceImpl implements CheckInOutService {

	@Autowired
	CheckInOutRepository checkInOutRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;

	DozerBeanMapper mapper = new DozerBeanMapper();

	public boolean compareDate(Date date, Date date1) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (date != null && date1 != null) {
			String compare1 = simpleDateFormat.format(date);
			String compare2 = simpleDateFormat.format(date1);
			return compare1.equals(compare2);
		}
		return false;

	}
//
	@Override
	public ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		String code = request.getHeader("token");
		if (code != null) {
			Token token = tokenRepository.getTokenByCode(code);
			User user = userRepository.getUserById(token.getId());
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(checkInOutDTO.getTimeCheck());
			CheckInOut checkInOut = new CheckInOut();
			int hourCheckIn = calendar.get(Calendar.HOUR_OF_DAY);
			int minuteCheckIn = calendar.get(Calendar.MINUTE);
//			System.out.println("gio: " + hourCheckIn);
//			System.out.println("Phut: " + minuteCheckIn);

			Date date = checkInOutRepository.getDate(user.getId());

			Date dateNow = new Date();

			if (compareDate(date, dateNow)) {
				return new ResponseEntity<>(Notifications.CHECKED, HttpStatus.OK);
			} else {
				if ((hourCheckIn == 8 && minuteCheckIn < 30) || (hourCheckIn < 8 && minuteCheckIn < 59)) {
					return new ResponseEntity<>(Notifications.CHECKIN_FAILED_BEFORE_8h30, HttpStatus.OK);
				} else if (hourCheckIn >= 8 && minuteCheckIn >= 30 && hourCheckIn <= 9) {
					calendar.set(Calendar.HOUR_OF_DAY, 9);
					calendar.set(Calendar.MINUTE, 0);
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setUser(user);
					checkInOutRepository.save(checkInOut);
					return new ResponseEntity<>(Notifications.CHECKIN_SUCCESS, HttpStatus.OK);
				} else if (hourCheckIn >= 9 && hourCheckIn <= 10 && minuteCheckIn <= 30) {
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setUser(user);

					checkInOutRepository.save(checkInOut);
					return new ResponseEntity<>(Notifications.CHECKIN_SUCCESS, HttpStatus.OK);
				} else if (hourCheckIn >= 10 && minuteCheckIn > 30 && hourCheckIn < 11) {
					calendar.set(Calendar.HOUR_OF_DAY, 13);
					calendar.set(Calendar.MINUTE, 0);
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setUser(user);
					checkInOutRepository.save(checkInOut);
					//  return new ResponseEntity<>("CHECKIN_FAILED_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<>(Notifications.CHECKIN_FAILED_AFTER_10h30, HttpStatus.OK);
				} else if (hourCheckIn == 12 && minuteCheckIn <= 59) {
					checkInOut.setUser(user);
					checkInOut.setDayCheckIn(calendar.getTime());
					calendar.set(Calendar.HOUR_OF_DAY, 13);
					calendar.set(Calendar.MINUTE, 0);
					checkInOut.setStartTime(calendar.getTime());
					checkInOutRepository.save(checkInOut);
					//return new ResponseEntity<String>("CHECKIN_SUCCESS_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<String>(Notifications.CHECKIN_SUCCESS, HttpStatus.OK);
				} else if (hourCheckIn >= 13 && hourCheckIn <= 16) {
					if (hourCheckIn == 16 && minuteCheckIn > 1) {
						//return new ResponseEntity<>("CHECKIN_FAILED_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
						return new ResponseEntity<>(Notifications.CHECKIN_FAILED_AFTER_16h, HttpStatus.OK);
					}
					checkInOut.setDayCheckIn(calendar.getTime());
					checkInOut.setStartTime(calendar.getTime());
					checkInOut.setUser(user);
					checkInOutRepository.save(checkInOut);
					//return new ResponseEntity<>("CHECKIN_SUCCESS_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<>(Notifications.CHECKIN_SUCCESS, HttpStatus.OK);

				} else {
					// return new ResponseEntity<>("TIMEOUT" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
					return new ResponseEntity<>(Notifications.TIMEOUT, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(Notifications.NOT_LOGGED_IN, HttpStatus.BAD_REQUEST);

	}

	@Override
	public ResponseEntity checkOut(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		String codeToken = request.getHeader("token");
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
				// cong them 60 neu hour check out >= 12
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
			return new ResponseEntity(Notifications.CHECKOUT_SUCCESS, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(Notifications.PLEASE_CHECKIN, HttpStatus.OK);
		}


	}

	@Override
	public ResponseEntity<List<CheckInOut>> getListCheckInOut(HttpServletRequest request) {
		String code = request.getHeader("token");
		Token token = tokenRepository.getTokenByCode(code);
		int userId = token.getId();
		List<CheckInOut> checkInOutList = checkInOutRepository.getListCheckInOutByIdUser(userId);
		return new ResponseEntity<List<CheckInOut>>(checkInOutList, HttpStatus.OK);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public PagedResponse<Checkin2Admin> pageGetAllCheckInsAllUserByDate(long date, int pageNumber, int pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<CheckInOut> page = checkInOutRepository.findCheckInOutsByDayCheckIn(pageable, new Date(date));

		List<CheckInOut> checkInOuts = page.getContent();
		List<Checkin2Admin> checkin2Admins = new ArrayList<>();

		checkInOuts.forEach(checkInOut -> {
			Checkin2Admin checkin2Admin = mapper.map(checkInOut, Checkin2Admin.class);

//          use Dozer?
			checkin2Admin.setName(checkInOut.getUser().getName());
			checkin2Admin.setUserId(checkInOut.getUser().getId());
			checkin2Admin.setPosition(Position.values()[checkInOut.getUser().getPosition()].toString());

			checkin2Admins.add(checkin2Admin);

		});

		return new PagedResponse<>(checkin2Admins, pageNumber, pageSize, checkInOuts.size(), page.getTotalPages(), page.isLast());
	}

	@Override
	public ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize) {
		return null;
	}

	@Override
	public ResponseEntity getACheckInById(int id, HttpServletRequest request) {
		return null;
	}

	@Override
	public ResponseEntity getCheckInOutAndPage(int page, int size, HttpServletRequest request) {
		return null;
	}

	@Override
	public boolean updateCheckInOut(CheckInOutDTO checkInOutDTO) {
		return false;
	}

	@Override
	public ResponseEntity getACheckInById(int id) {
		return null;
	}

////	@SuppressWarnings("Duplicates")
////	@Override
////	public ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize) {
////		Pageable pageable = PageRequest.of(pageNumber, pageSize);
////		Page<CheckInOut> page = checkInOutRepository.findCheckInOutsByDayCheckInAndUserId(pageable, new Date(startDate), new Date(endDate), idUser);
////		List<CheckInOut> checkInOuts = page.getContent();
////		List<CheckInOutDTO> checkInOutDTOS = new ArrayList<>();
////		checkInOuts.forEach(checkInOut -> checkInOutDTOS.add(mapper.map(checkInOut, CheckInOutDTO.class)));
////
////		return new ResponseEntity(checkInOutDTOS, HttpStatus.OK);
////	}
////
////	public boolean updateCheckInOut(CheckInOutDTO checkInOutDTO) {
////		long timeCheckIn = checkInOutDTO.getStartTime();
////		System.out.println(timeCheckIn);
////		long timeCheckOut = checkInOutDTO.getEndTime();
////		System.out.println(timeCheckOut);
////		CheckInOut checkInOut = checkInOutRepository.getCheckInOutById(checkInOutDTO.getId());
////		if (checkInOut != null) {
////			Calendar checkIn = Calendar.getInstance();
////			Calendar checkOut = Calendar.getInstance();
////			checkIn.setTimeInMillis(timeCheckIn);
////			checkOut.setTimeInMillis(timeCheckOut);
////			int total = calculateTotal(checkIn.getTime(), checkOut.getTime());
////			if (checkIn.get(Calendar.HOUR_OF_DAY) < 12 && checkOut.get(Calendar.HOUR_OF_DAY) > 13) {
////				total = total - 60;
////			}
////			checkInOut.setStartTime(checkIn.getTime());
////			checkInOut.setEndTime(checkOut.getTime());
////			checkInOut.setTotalTime(total);
////			return true;
////		}
////		return false;
////
////	}
////
////	public int calculateTotal(Date checkIn, Date checkOut) {
////		long timeCheckIn = checkIn.getTime();
////		long timeCheckOut = checkOut.getTime();
////
////		int total = (int) ((timeCheckOut - timeCheckIn) / 1000) / 60;
////
////		return total;
////	}
////
////
////	@Override
////	public ResponseEntity getACheckInById(int id, HttpServletRequest request) {
////		User user = getUserInHeader(request);
////		BeanMappingBuilder beanMappingBuilder = getBeanMappingBuilder();
////		DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
////		dozerBeanMapper.addMapping(beanMappingBuilder);
////		CheckInOut checkInOut = checkInOutRepository.getOne(id);
////		CheckInOutDTO checkInOutDTO = dozerBeanMapper.map(checkInOut, CheckInOutDTO.class);
////		dozerBeanMapper.map(user, checkInOutDTO);
////		return new ResponseEntity(checkInOutDTO, HttpStatus.OK);
////	}
////
////	public BeanMappingBuilder getBeanMappingBuilder() {
////		BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
////			@Override
////			protected void configure() {
////				mapping(CheckInOut.class, CheckInOutDTO.class).fields("id", "id").fields("dayCheckIn", "dayCheckIn")
////						.fields("totalTime", "totalTime").exclude("user").fields("user.id","id_user");
////
////				mapping(User.class, CheckInOutDTO.class).fields("name", "name").fields("position", "position");;
////			}
////		};
////
////		return beanMappingBuilder;
////	}
////
////	public User getUserInHeader(HttpServletRequest request) {
////		String code = request.getHeader("token");
////		Token token = tokenRepository.getTokenByCode(code);
////		User user = userRepository.getUserById(token.getId());
////
////		return user;
////	}
////
////	@Override
////	public ResponseEntity getCheckInOutAndPage(int page, int size, HttpServletRequest request) {
////		User user = getUserInHeader(request);
////		BeanMappingBuilder beanMappingBuilder = getBeanMappingBuilder();
////		Pageable pageable = PageRequest.of(page, size);
////		Page<CheckInOut> pageCheckInOut = checkInOutRepository.getCheckInOutByUserAndPage(user, pageable);
////		List<CheckInOutDTO> checkInOutDTOList = new LinkedList<>();
////		DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
////		dozerBeanMapper.addMapping(beanMappingBuilder);
////		pageCheckInOut.getContent().forEach(checkInOut -> {
////			CheckInOutDTO checkInOutDTO = dozerBeanMapper.map(checkInOut, CheckInOutDTO.class);
////			dozerBeanMapper.map(user, checkInOutDTO);
////			System.out.println(checkInOut.getStartTime());
////			checkInOutDTOList.add(checkInOutDTO);
////		});
////		return new ResponseEntity(checkInOutDTOList, HttpStatus.OK);
////	}
//
//	@Override
//	public ResponseEntity getACheckInById(int id) {
//
//		return new ResponseEntity(mapper.map(checkInOutRepository.getOne(id), CheckInOutDTO.class), HttpStatus.OK);
//
//	}


}
