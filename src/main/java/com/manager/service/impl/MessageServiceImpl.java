package com.manager.service.impl;

import com.manager.data.Notifications;
import com.manager.dto.CheckInOutDTO;
import com.manager.dto.LeaveApplicationDTO;
import com.manager.dto.MessageDemoDTO;
import com.manager.dto.RequestMessageDTO;
import com.manager.model.*;
import com.manager.repository.*;
import com.manager.service.MessageService;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    MessageDemoRepository messageDemoRepository;
    @Autowired
    LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    CheckInOutRepository checkInOutRepository;
    //get user with token
    public User getUser(HttpServletRequest request) {
        String code = request.getHeader("token");
        Token token = tokenRepository.getTokenByCode(code);
        User user = userRepository.getUserById(token.getId());
        return user;
    }

    public RequestMessageDTO requestEditCheckInOut(RequestMessageDTO requestMessageDTO, HttpServletRequest request) {
        User user = getUser(request);
        //type == 0: edit check in check out
        //type == 1: leave application
        if (requestMessageDTO.getType() == 0) {
            MessageDemo messageDemo = new MessageDemo();
            messageDemo.setIdReport(requestMessageDTO.getIdCheckInOut());
            String title = "User: " + user.getName() +"(id: " + user.getId()+ ")" + " Request edit checkInOut time";
            messageDemo.setTitle(title);
            messageDemo.setContent(requestMessageDTO.getContent());
            messageDemo.setFrom(user);
            messageDemo.setType(0);

            //idReport constant a record checkInOut
            messageDemo.setIdReport(requestMessageDTO.getIdCheckInOut());

            //send for admin
            // role = 1 is admin
            userRepository.getRoleUser(1).forEach(user1 -> {
                messageDemo.setTo(user1);
                messageDemoRepository.save(messageDemo);
            });
        }
        return requestMessageDTO;
    }

    public ResponseEntity<List<MessageDemoDTO>> getAllMessageUnread(HttpServletRequest request, int type) {
        User user = getUser(request);
        List<MessageDemoDTO> messageDemoDTOList = new LinkedList<MessageDemoDTO>();
        for (MessageDemo messageDemo : messageDemoRepository.getAllMessageByStatusAndToAndType(false, user, type)) {
            MessageDemoDTO messageDemoDTO = convertToMessageDemoDTO(messageDemo);
            messageDemoDTOList.add(messageDemoDTO);
        }
        return new ResponseEntity<>(messageDemoDTOList, HttpStatus.OK);

    }

    public ResponseEntity<List<MessageDemoDTO>> getAllMessageUnreadPage(HttpServletRequest request, int page, int size) {
        User user = getUser(request);
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageDemo> pageMessageDemo = messageDemoRepository.getAllMessageByStatusAndToAndTypePage(pageable, false, user);
        List<MessageDemoDTO> messageDemoDTOList = new LinkedList<>();
        pageMessageDemo.getContent().forEach(messageDemo -> {
            MessageDemoDTO messageDemoDTO = convertToMessageDemoDTO(messageDemo);
            messageDemoDTOList.add(messageDemoDTO);
        });

        return new ResponseEntity<>(messageDemoDTOList, HttpStatus.OK);
    }

    public MessageDemoDTO convertToMessageDemoDTO(MessageDemo messageDemo) {
        MessageDemoDTO messageDemoDTO = new MessageDemoDTO();
        messageDemoDTO.setId(messageDemo.getId());
        messageDemoDTO.setContent(messageDemo.getContent());
        messageDemoDTO.setTitle(messageDemo.getTitle());
        messageDemoDTO.setTo(messageDemo.getTo().getName());
        messageDemoDTO.setFrom(messageDemo.getFrom().getName());
        messageDemoDTO.setStatus(messageDemo.getStatus());
        if (messageDemo.getType() == 0) {
            messageDemoDTO.setType(Notifications.REQUEST_EDIT_CHECKINOUT);
        } else if (messageDemo.getType() == 1) {
            messageDemoDTO.setType(Notifications.REQUEST_A_DAY_OFF);
        }
        messageDemoDTO.setIdRecord(messageDemo.getIdReport());
        messageDemoDTO.setId(messageDemo.getId());
        long time = messageDemo.getTimeRequest().getTime();
        messageDemoDTO.setTimeRequest(time);
        return messageDemoDTO;
    }

    public ResponseEntity readAll(HttpServletRequest request) {
        User user = getUser(request);
        for (MessageDemo messageDemo : messageDemoRepository.getAllMessageByStatusAndToAndType(false, user, 1)) {
            messageDemo.setStatus(true);
        }
        return new ResponseEntity(true, HttpStatus.OK);
    }

    public ResponseEntity readAMessage(int id, HttpServletRequest request) {
        User user = getUser(request);
        MessageDemo messageDemo = messageDemoRepository.getMessageDemoByToAndId(user, id);
        if (messageDemo == null) {
            return new ResponseEntity(Notifications.MESSAGE_NOT_EXITS, HttpStatus.BAD_REQUEST);
        }
        CheckInOut checkInOut = checkInOutRepository.getCheckInOutById(messageDemo.getIdReport());
        LeaveApplication leaveApplication = leaveApplicationRepository.getLeaveApplicationsById(messageDemo.getIdReport());
        if(leaveApplication != null && messageDemo.getType() == 1) {
            messageDemo.setStatus(true);
            LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationServiceImpl().convertToLeaveApplicationDTO(leaveApplication);
            return new ResponseEntity(leaveApplicationDTO, HttpStatus.OK);
        }else if(checkInOut != null && messageDemo.getType() == 0){
            BeanMappingBuilder beanMappingBuilder = new CheckInOutServiceImpl().getBeanMappingBuilder();
            DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
            dozerBeanMapper.addMapping(beanMappingBuilder);
            CheckInOutDTO checkInOutDTO = dozerBeanMapper.map(checkInOut, CheckInOutDTO.class);
            return new ResponseEntity(checkInOutDTO, HttpStatus.OK);
        }
        else{
            messageDemoRepository.delete(messageDemo);
            return new ResponseEntity(Notifications.MESSAGE_NOT_EXITS, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity processLeaveApplication(int id, int accept, HttpServletRequest request) {
        User user = getUser(request);
        MessageDemo messageDemo = messageDemoRepository.getMessageDemoByToAndId(user, id);
        if (messageDemo == null) {
            return new ResponseEntity(Notifications.MESSAGE_NOT_EXITS, HttpStatus.BAD_REQUEST);
        }

        // get id of leave application
        int idLeaveApplication = messageDemo.getIdReport();
        LeaveApplication leaveApplication = leaveApplicationRepository.getLeaveApplicationsById(idLeaveApplication);
        if (leaveApplication == null) {
            return new ResponseEntity(Notifications.LEAVE_APPLICATION_NOT_EXITS, HttpStatus.BAD_REQUEST);
        }
        //reply result to sender
        MessageDemo messageReply = new MessageDemo();
        messageReply.setFrom(user);
        messageReply.setTo(messageDemo.getFrom());
        if (accept == 1) {
            messageReply.setTitle(user.getName() + " accepted leave application of you");
            leaveApplication.setStatus("accept");

        } else if (accept == 0) {
            messageReply.setTitle(user.getName() + " not accept leave application of you");
            leaveApplication.setStatus("not accept");
        } else {
            return new ResponseEntity(Notifications.ERROR, HttpStatus.BAD_REQUEST);
        }
        messageReply.setStatus(false);
        messageReply.setIdReport(idLeaveApplication);
        messageReply.setType(1);
        messageDemo.setStatus(true);
        messageDemoRepository.save(messageDemo);
        messageDemoRepository.save(messageReply);
//        leaveApplicationRepository.save(leaveApplication);
        LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationServiceImpl().convertToLeaveApplicationDTO(leaveApplication);
        return new ResponseEntity(leaveApplicationDTO, HttpStatus.OK);
    }

    public ResponseEntity messages(int size, int page, HttpServletRequest request){
        Pageable pageable = PageRequest.of(size, page);
        User user = getUser(request);
        List<MessageDemoDTO> messageDemoDTOList = new LinkedList<>();
        Page<MessageDemo> pageMessage = messageDemoRepository.getMessageDemoByTo(pageable, user);
        pageMessage.getContent().forEach(messageDemo -> {
            MessageDemoDTO messageDemoDTO = convertToMessageDemoDTO(messageDemo);
            messageDemoDTOList.add(messageDemoDTO);
        });

        return new ResponseEntity(messageDemoDTOList, HttpStatus.OK);
    }
}
