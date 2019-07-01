package com.manager.service.Impl;

import com.manager.dto.ListADayOffDTO;
import com.manager.dto.RequestADayOffDTO;
import com.manager.model.LeaveApplication;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.LeaveApplicationRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

@Service
public class LeaveApplicationServiceImpl {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public ResponseEntity requestADayOff(RequestADayOffDTO requestADayOffDTO, HttpServletRequest request) {
        String codeToken = (String) request.getSession().getAttribute("token");

        if (codeToken == null) {
            return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);
        }
        Token token = tokenRepository.getTokenByCode(codeToken);
        User user = userRepository.getUserById(token.getId());
        if (user != null) {
            Calendar calendarFromDate = Calendar.getInstance();
            Calendar calendarToDate = Calendar.getInstance();

            calendarFromDate.setTimeInMillis(requestADayOffDTO.getFromDate());
            calendarToDate.setTimeInMillis(requestADayOffDTO.getToDate());

            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setStartTime(calendarFromDate.getTime());
            leaveApplication.setEndTime(calendarToDate.getTime());
            leaveApplication.setReason(requestADayOffDTO.getReason());
            leaveApplication.setStatus("pending");
            leaveApplication.setUser(user);

            leaveApplicationRepository.save(leaveApplication);
            return new ResponseEntity("REQUESTED", HttpStatus.OK);
        }
        return new ResponseEntity("ERROR_REQUEST", HttpStatus.OK);
    }

    public ResponseEntity<List<LeaveApplication>> listDayOff(ListADayOffDTO listADayOffDTO, HttpServletRequest request) {
        String code = (String) request.getSession().getAttribute("token");
        if (code == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Token token1 = tokenRepository.getTokenByCode(code);
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getListApplicationInWeek(listADayOffDTO.getMonth(), token1.getId());
        return new ResponseEntity<>(leaveApplicationList, HttpStatus.OK);
    }
}
