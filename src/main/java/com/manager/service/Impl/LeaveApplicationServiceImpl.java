package com.manager.service.Impl;

import com.manager.dto.LeaveApplicationDTO;
import com.manager.dto.ListADayOffDTO;
import com.manager.dto.RequestADayOffDTO;
import com.manager.model.LeaveApplication;
import com.manager.model.Message;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.LeaveApplicationRepository;
import com.manager.repository.MessageRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
public class LeaveApplicationServiceImpl {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MessageRepository messageRepository;

    public ResponseEntity<RequestADayOffDTO> requestADayOff(RequestADayOffDTO requestADayOffDTO, HttpServletRequest request) {
        String codeToken = request.getHeader("access_Token");
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

            leaveApplication = leaveApplicationRepository.save(leaveApplication);

            Message message = new Message(false, leaveApplication);
            message.setMessage(user.getName() + " request a day off");
            messageRepository.save(message);
            requestADayOffDTO.setStatus("pending");
            return new ResponseEntity(requestADayOffDTO, HttpStatus.OK);
        }
        return new ResponseEntity("ERROR_REQUEST", HttpStatus.OK);
    }

    public ResponseEntity<List<LeaveApplication>> listDayOff(ListADayOffDTO listADayOffDTO, HttpServletRequest request) {
        String code = request.getHeader("access_Token");
        if (code == null) {
            return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);
        }
        Token token1 = tokenRepository.getTokenByCode(code);
        int idUser = token1.getId();
        int month = listADayOffDTO.getMonth();
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getListApplicationInWeek(month, idUser);
        return new ResponseEntity<>(leaveApplicationList, HttpStatus.OK);
    }

    public LeaveApplicationDTO convertToLeaveApplicationDTO(LeaveApplication leaveApplication){
        LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
        leaveApplicationDTO.setName(leaveApplication.getUser().getName());
        long fromDate = leaveApplication.getStartTime().getTime();
        long toDate = leaveApplication.getEndTime().getTime();
        leaveApplicationDTO.setFromDate(fromDate);
        leaveApplicationDTO.setToDate(toDate);
        return leaveApplicationDTO;
    }

    public ResponseEntity<List<LeaveApplicationDTO>> getListApplicationDTO(){
        List<LeaveApplicationDTO> leaveApplicationDTOS = new LinkedList<LeaveApplicationDTO>();
        for(LeaveApplication leaveApplication : leaveApplicationRepository.findAll()){
            LeaveApplicationDTO leaveApplicationDTO = convertToLeaveApplicationDTO(leaveApplication);
            leaveApplicationDTOS.add(leaveApplicationDTO);
        }
        return new ResponseEntity<List<LeaveApplicationDTO>>(leaveApplicationDTOS, HttpStatus.OK);
    }

}
