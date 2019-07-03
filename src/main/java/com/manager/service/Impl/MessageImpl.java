package com.manager.service.Impl;

import com.manager.dto.MessageDemoDTO;
import com.manager.dto.RequestADayOffDTO;
import com.manager.dto.RequestMessageDTO;
import com.manager.model.*;
import com.manager.repository.MessageDemoRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
@Service
public class MessageImpl {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    MessageDemoRepository messageDemoRepository;

    public User getUser(HttpServletRequest request){
        String code = request.getHeader("access_Token");
        Token token = tokenRepository.getTokenByCode(code);
        User user = userRepository.getUserById(token.getId());
        return user;
    }

    public RequestMessageDTO requestMessageDTO(RequestMessageDTO requestMessageDTO, HttpServletRequest request){
        User user = getUser(request);
        if(requestMessageDTO.getType() == 1){
            MessageDemo messageDemo = new MessageDemo();
            messageDemo.setMessage("ID: " + "(" + user.getName()+")" +" Request edit checkin time");
            messageDemo.setContent(requestMessageDTO.getContent());
            messageDemo.setFrom(user);
            messageDemo.setType("EDIT_CHECKOUT");
            for(User user1 : userRepository.getRoleUser(2)){
                messageDemo.setTo(user1);
                messageDemoRepository.save(messageDemo);
            }
        }
        else if(requestMessageDTO.getType() == 2){
            MessageDemo messageDemo = new MessageDemo();
            messageDemo.setMessage("ID: " + user.getId() +"("+user.getName()+")" +" Request edit checkOut time");
            messageDemo.setContent(requestMessageDTO.getContent());
            messageDemo.setFrom(user);
            messageDemo.setType("EDIT_CHECKOUT");
            for(User user1 : userRepository.getRoleUser(2)){
                messageDemo.setTo(user1);
                messageDemoRepository.save(messageDemo);
            }
        }
        else if(requestMessageDTO.getType() == 0){
            MessageDemo messageDemo = new MessageDemo();
            messageDemo.setMessage("ID: " + user.getId() +"("+user.getName()+")" +" Request a day off");
            messageDemo.setFrom(user);
            messageDemo.setContent(requestMessageDTO.getContent());
            messageDemo.setType("LEAVE_APPLICATION");
            messageDemo.setIdLeaveApplication(requestMessageDTO.getLeaveApplication());
            for(User user1 : userRepository.getRoleUser(3)){
                messageDemo.setTo(user1);
                messageDemoRepository.save(messageDemo);
            }
        }
        return requestMessageDTO;
    }

    public ResponseEntity<List<MessageDemoDTO>> getAllMessageUnread(HttpServletRequest request){
        User user = getUser(request);
        List<MessageDemoDTO> messageDemoDTOList = new LinkedList<MessageDemoDTO>();
        for(MessageDemo messageDemo : messageDemoRepository.getAllMessageByStatusAndTo(false, user)){
            MessageDemoDTO messageDemoDTO = convertToMessageDemoDTO(messageDemo);
            messageDemoDTOList.add(messageDemoDTO);
        }
        return new ResponseEntity<>(messageDemoDTOList, HttpStatus.OK);

    }

    public MessageDemoDTO convertToMessageDemoDTO(MessageDemo messageDemo){
        MessageDemoDTO messageDemoDTO = new MessageDemoDTO();
        messageDemoDTO.setContent(messageDemo.getContent());
        messageDemoDTO.setMessage(messageDemo.getMessage());
        messageDemoDTO.setTo(messageDemo.getTo().getName());
        messageDemoDTO.setFrom(messageDemo.getFrom().getName());
        messageDemoDTO.setType(messageDemo.getType());
        messageDemoDTO.setId(messageDemo.getId());
        long time = messageDemo.getCreatedTime().getTime();
        messageDemoDTO.setCreatedTime(time);

        return  messageDemoDTO;
    }

    public ResponseEntity readAll(HttpServletRequest request){
        User user = getUser(request);
        for(MessageDemo messageDemo : messageDemoRepository.getAllMessageByStatusAndTo(false, user)){
            messageDemo.setStatus(true);
            messageDemoRepository.save(messageDemo);
        }
        return new ResponseEntity(true, HttpStatus.OK);
    }
}
