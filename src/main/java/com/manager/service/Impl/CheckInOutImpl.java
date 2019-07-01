package com.manager.service.Impl;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.LoginDTO;
import com.manager.dto.RequestADayOffDTO;
import com.manager.dto.ResetPasswordDTO;
import com.manager.model.CheckInOut;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.CheckInOutRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class CheckInOutImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckInOutRepository checkInOutRepository;

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

    public ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {

        String code = (String) request.getSession().getAttribute("token");
        if (code != null) {
            Token token = tokenRepository.getTokenByCode(code);
            User user = userRepository.findById(token.getId()).get();
            System.out.println(user);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(checkInOutDTO.getTimeCheck());
            CheckInOut checkInOut = new CheckInOut();
            int hourCheckIn = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteCheckIn = calendar.get(Calendar.MINUTE);
            System.out.println("gio: " + hourCheckIn);
            System.out.println("Phut: " + minuteCheckIn);

            Date date = checkInOutRepository.getDate();

            Date date1 = new Date();

            if (compareDate(date, date1)) {
                return new ResponseEntity<>("Checked", HttpStatus.OK);
            } else {
                if (calendar.get(Calendar.HOUR_OF_DAY) < 8 && calendar.get(Calendar.MINUTE) < 30) {
                    return new ResponseEntity<>("FAILED_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                } else if (hourCheckIn >= 8 && minuteCheckIn >= 30 && hourCheckIn <= 9) {
                    calendar.set(Calendar.HOUR_OF_DAY, 9);
                    calendar.set(Calendar.MINUTE, 0);
                    checkInOut.setStartTime(calendar.getTime());
                    checkInOut.setDayCheckIn(calendar.getTime());
                    checkInOut.setUser(user);
                    checkInOutRepository.save(checkInOut);
                    return new ResponseEntity<>("CHECKIN_SUCCESS" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                } else if (hourCheckIn >= 9 && hourCheckIn <= 10 && minuteCheckIn <= 30) {
                    checkInOut.setDayCheckIn(calendar.getTime());
                    checkInOut.setStartTime(calendar.getTime());
                    checkInOut.setUser(user);

                    checkInOutRepository.save(checkInOut);
                    return new ResponseEntity<>("CHECKIN_SUCCESS" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                } else if (hourCheckIn >= 10 && minuteCheckIn > 30 && hourCheckIn < 11) {
                    calendar.set(Calendar.HOUR_OF_DAY, 13);
                    calendar.set(Calendar.MINUTE, 0);
                    checkInOut.setDayCheckIn(calendar.getTime());
                    checkInOut.setStartTime(calendar.getTime());
                    checkInOut.setUser(user);
                    checkInOutRepository.save(checkInOut);
                    return new ResponseEntity<>("FAILED_HOURS_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                } else if (hourCheckIn == 12 && minuteCheckIn <= 59) {
                    checkInOut.setUser(user);
                    checkInOut.setDayCheckIn(calendar.getTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 13);
                    calendar.set(Calendar.MINUTE, 0);
                    checkInOut.setStartTime(calendar.getTime());
                    checkInOutRepository.save(checkInOut);
                    return new ResponseEntity<String>("CHECKIN_SUCCESS_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                } else if (hourCheckIn >= 13 && hourCheckIn <= 16) {
                    if (hourCheckIn == 16 && minuteCheckIn > 1) {
                        return new ResponseEntity<>("CHECKIN_FAILED_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                    }
                    checkInOut.setDayCheckIn(calendar.getTime());
                    checkInOut.setStartTime(calendar.getTime());
                    checkInOut.setUser(user);
                    checkInOutRepository.save(checkInOut);
                    return new ResponseEntity<>("CHECKIN_SUCCESS_" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("FAILED_OTHER" + hourCheckIn + "_" + minuteCheckIn, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity checkOut(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
        String codeToken = (String) request.getSession().getAttribute("token");
        if (codeToken == null) {
            return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);
        }
        Token token = tokenRepository.getTokenByCode(codeToken);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dateNow = Calendar.getInstance();
        String dateSearch = simpleDateFormat.format(dateNow.getTime());
        int totalMillis = 0;
        //search to day
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
            double totalTime = (totalMillis / 60) - 60f;
           /* System.out.println("Total Millis: " + totalMillis);
            int h = totalMillis / 3600;
            int m = totalMillis % 3600 / 60;
            int s = totalMillis % 3600 % 60;
            System.out.println("h: " + h +" m: " + m + " s: " + s);
*/
            checkInOut.setEndTime(calendar.getTime());
            checkInOut.setTotalTime(totalTime);

            checkInOutRepository.save(checkInOut);

            return new ResponseEntity("SUCCESS", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("PLEASE_CHECKIN", HttpStatus.OK);
        }


    }


}
