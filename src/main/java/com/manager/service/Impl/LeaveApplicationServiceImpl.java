package com.manager.service.Impl;

import com.manager.dto.*;
import com.manager.model.*;
import com.manager.repository.*;
import com.manager.service.LeaveApplicationService;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    MessageDemoRepository messageDemoRepository;
    @Autowired
    CheckInOutRepository checkInOutRepository;

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
            MessageDemo messageDemo = new MessageDemo();
            messageDemo.setIdReport(leaveApplication.getId());
            messageDemo.setTitle("User: " + user.getId() + "(" + user.getName() + ") request a day off");
            messageDemo.setStatus(false);
            messageDemo.setFrom(user);
            //type = 1: leave application
            // type = 0: edit checkinout
            messageDemo.setType(1);

            //send to all manager(role 3)
            userRepository.getRoleUser(3).forEach(user1 -> {
                messageDemo.setTo(user1);
                messageDemoRepository.save(messageDemo);
            });
            return new ResponseEntity(requestADayOffDTO, HttpStatus.OK);
        }
        return new ResponseEntity("ERROR_REQUEST", HttpStatus.OK);
    }

    public ResponseEntity<List<LeaveApplication>> listDayOff(int month, HttpServletRequest request) {
        String code = request.getHeader("access_Token");
        if (code == null) {
            return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.BAD_REQUEST);
        }
        Token token1 = tokenRepository.getTokenByCode(code);
        int idUser = token1.getId();
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getListApplicationInWeek(month, idUser);
        List<LeaveApplicationDTO> leaveApplicationDTOS = new LinkedList<>();
        leaveApplicationList.forEach(leaveApplication -> {

            BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
                @Override
                protected void configure() {
                    mapping(LeaveApplication.class, LeaveApplicationDTO.class).fields("startTime", "fromDate").fields("endTime", "toDate");
                }
            };

            DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
            dozerBeanMapper.addMapping(beanMappingBuilder);
            LeaveApplicationDTO leaveApplicationDTO1 = dozerBeanMapper.map(leaveApplication, LeaveApplicationDTO.class);
            leaveApplicationDTOS.add(leaveApplicationDTO1);

        });
        return new ResponseEntity(leaveApplicationDTOS, HttpStatus.OK);
    }

    public LeaveApplicationDTO convertToLeaveApplicationDTO(LeaveApplication leaveApplication) {
        LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
        leaveApplicationDTO.setName(leaveApplication.getUser().getName());
        long fromDate = leaveApplication.getStartTime().getTime();
        long toDate = leaveApplication.getEndTime().getTime();
        leaveApplicationDTO.setFromDate(fromDate);
        leaveApplicationDTO.setToDate(toDate);
        leaveApplicationDTO.setReason(leaveApplication.getReason());
        leaveApplicationDTO.setId(leaveApplication.getId());
        leaveApplicationDTO.setStatus(leaveApplication.getStatus());
/*
        leaveApplicationDTO.setPosition(leaveApplication.getUser().getPosition());
*/
        return leaveApplicationDTO;
    }

    public ResponseEntity<List<LeaveApplicationDTO>> getListApplicationDTO() {
        List<LeaveApplicationDTO> leaveApplicationDTOS = new LinkedList<LeaveApplicationDTO>();
        for (LeaveApplication leaveApplication : leaveApplicationRepository.findAll()) {
            LeaveApplicationDTO leaveApplicationDTO = convertToLeaveApplicationDTO(leaveApplication);
            leaveApplicationDTOS.add(leaveApplicationDTO);
        }
        return new ResponseEntity<List<LeaveApplicationDTO>>(leaveApplicationDTOS, HttpStatus.OK);
    }

    public ResponseEntity<Object> viewMessage(RequestMessageDTO requestMessageDTO) {
        System.out.println("Type: " + requestMessageDTO.getType());
        if (requestMessageDTO.getType() == 1) {
            LeaveApplication leaveApplication = leaveApplicationRepository.findById(requestMessageDTO.getIdCheckInOut()).get();
            if(leaveApplication == null){
                return new ResponseEntity<>("MESSAGE_NOT_EXITS", HttpStatus.NOT_FOUND);
            }
            LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
            leaveApplicationDTO.setName(leaveApplication.getUser().getName());
            leaveApplicationDTO.setReason(leaveApplication.getReason());
            leaveApplicationDTO.setToDate(leaveApplication.getStartTime().getTime());
            leaveApplicationDTO.setFromDate(leaveApplication.getEndTime().getTime());
            System.out.println(leaveApplication.getReason());
            return new ResponseEntity<Object>(leaveApplicationDTO, HttpStatus.OK);
        } else if (requestMessageDTO.getType() == 0) {
            CheckInOut checkInOut = checkInOutRepository.getOne(requestMessageDTO.getIdCheckInOut());
            if(checkInOut == null){
                return new ResponseEntity<>("MESSAGE_NOT_EXITS", HttpStatus.NOT_FOUND);
            }
            System.out.println(checkInOut.getStartTime());
            CheckInOutDTO checkInOutDTO = new CheckInOutDTO();
            checkInOutDTO.setCheckin(checkInOut.getStartTime().getTime());
            checkInOutDTO.setCheckout(checkInOut.getEndTime().getTime());
            checkInOutDTO.setId(checkInOutDTO.getId());
            checkInOutDTO.setName(checkInOut.getUser().getName());
            checkInOutDTO.setTotal(checkInOut.getTotalTime());
            return new ResponseEntity<Object>(checkInOutDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("MESSAGE_NOT_EXITS", HttpStatus.OK);
        }
    }

    public ResponseEntity listDayOffPage(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request){
        String code = request.getHeader("access_Token");
        Token token = tokenRepository.getTokenByCode(code);
        User user = userRepository.getUserById(token.getId());
        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveApplication> leaveApplicationPage = leaveApplicationRepository.getLeaveApplicationByPage(pageable, user);

        List<LeaveApplicationDTO> leaveApplicationDTOS = new LinkedList<>();
        leaveApplicationPage.forEach(leaveApplication ->{
            LeaveApplicationDTO leaveApplicationDTO = convertToLeaveApplicationDTO(leaveApplication);
            leaveApplicationDTOS.add(leaveApplicationDTO);
        });

        return new ResponseEntity(leaveApplicationDTOS, HttpStatus.OK);
    }
}
