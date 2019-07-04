package com.manager.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import com.manager.dto.*;
import com.manager.model.*;
import com.manager.repository.MessageRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.Impl.CheckInOutServiceImpl;
import com.manager.service.Impl.LeaveApplicationServiceImpl;
import com.manager.service.Impl.MessageImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manager.service.Impl.UserServiceImpl;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private LeaveApplicationServiceImpl leaveApplicationService;

    @Autowired
    private CheckInOutServiceImpl checkInOutServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO,
                                        HttpServletRequest request) {
        return userServiceImpl.login(loginDTO, request);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        return userServiceImpl.logOut(request);
    }

    //get profile
    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> profile(HttpServletRequest request) {
        return userServiceImpl.profile(request);
    }

    //update profile
    @PostMapping("/profile")
    public ResponseEntity updateProfile(@RequestBody ProfileDTO profileDTO, HttpServletRequest request) {
        return userServiceImpl.updateProfile(profileDTO, request);
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        return userServiceImpl.uploadFile(multipartFile, request);
    }

    //Reset Password, send mail
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return userServiceImpl.forgotPassword(loginDTO, request);
    }

    //reset password in mail when request forgot password
    @GetMapping("/forgotPassword/{code}/{id}")
    public ResponseEntity<String> resetPasswordInMail(@RequestBody ResetPasswordDTO resetPasswordDTO, @PathVariable("code") String code, @PathVariable("id") int id) {
        return userServiceImpl.resetPassword(resetPasswordDTO, code, id);
    }

    //Change password in profile
    @PutMapping("/changePassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {
        return userServiceImpl.changePassword(resetPasswordDTO, request);
    }

    @PostMapping("/checkIn")
    public ResponseEntity<String> checkIn(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
        return checkInOutServiceImpl.checkIn(checkInOutDTO, request);
    }

    @PostMapping("/checkOut")
    public ResponseEntity CheckOut(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
        return checkInOutServiceImpl.checkOut(checkInOutDTO, request);
    }

    //get list checkInOuts
    @GetMapping("/checkInOuts")
    public ResponseEntity<List<CheckInOut>> checkInOuts(HttpServletRequest request) {
        return checkInOutServiceImpl.getListCheckInOut(request);
    }

    @PostMapping("/requestADayOff")
    public ResponseEntity requestADayOff(@RequestBody RequestADayOffDTO requestADayOffDTO, HttpServletRequest request) {
        return leaveApplicationService.requestADayOff(requestADayOffDTO, request);
    }

    @GetMapping("/listDayOff")
    public ResponseEntity<List<LeaveApplication>> listDayOff(@RequestBody ListADayOffDTO listADayOffDTO, HttpServletRequest request) {
        return leaveApplicationService.listDayOff(listADayOffDTO, request);
    }

    @GetMapping("test-token")
    public String s(HttpServletRequest request) {
        System.out.println(request.getHeader("access_Token"));
        return request.getHeader("access_Token");
    }

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin/hello")
    public User admin(HttpServletRequest request) {
        String code = request.getHeader("access_Token");
        Token token = tokenRepository.getTokenByCode(code);
        if (token != null) {
            return userRepository.getUserById(token.getId());
        }
        return null;
    }

    @GetMapping("/manager/hello")
    public User manager(HttpServletRequest request) {
        String code = request.getHeader("access_Token");
        Token token = tokenRepository.getTokenByCode(code);
        return userRepository.getUserById(token.getId());
    }

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("admin/message")
    public ResponseEntity<List<Message>> message() {
        List<Message> messageList = messageRepository.findAll();
        return new ResponseEntity<>(messageList, HttpStatus.NOT_FOUND);
    }

    @GetMapping("admin/message/{status}")
    public ResponseEntity<List<MessageDTO>> getListMessage(@PathVariable("status") int status) {
        List<MessageDTO> messageDTOList = new LinkedList<MessageDTO>();
        if (status >= 1) {
            List<Message> messageList = messageRepository.getListMessageByStatus(true);
            for (Message message : messageList) {
                MessageDTO messageDTO = convertMessageDTO(message);
                messageDTOList.add(messageDTO);
            }
            return new ResponseEntity<List<MessageDTO>>(messageDTOList, HttpStatus.OK);
        } else {
            List<Message> messageList = messageRepository.getListMessageByStatus(false);
            for (Message message : messageList) {
                MessageDTO messageDTO = convertMessageDTO(message);
                messageDTOList.add(messageDTO);
            }
            return new ResponseEntity<List<MessageDTO>>(messageDTOList, HttpStatus.OK);
        }
    }

    public MessageDTO convertMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setMessage(message.getMessage());
        messageDTO.setReason(message.getLeaveApplication().getReason());
        messageDTO.setStatus(message.getLeaveApplication().getStatus());
        long startDate = message.getLeaveApplication().getStartTime().getTime();
        long endDate = message.getLeaveApplication().getEndTime().getTime();
        long createdDate = message.getCreatedTime().getTime();
        messageDTO.setCreatedDate(createdDate);
        messageDTO.setStartDate(startDate);
        messageDTO.setEndDate(endDate);
        messageDTO.setIdApplication(message.getLeaveApplication().getId());
        messageDTO.setName(message.getLeaveApplication().getUser().getName());
        return messageDTO;
    }

  /*  @PostMapping("/admin/message/{id}")
    public ResponseEntity changeStatus(@PathVariable("id") int id){
        Message message = messageRepository.getOne(id);
        if(message != null){
            message.getLeaveApplication();
        }
        return null;
    }*/

    @GetMapping("admin/changeStatus")
    public ResponseEntity changeStatus(@RequestBody MessageDTO messageDTO) {
        Message message = messageRepository.getMessageById(messageDTO.getId());
        if (message != null) {
            message.setStatus(true);
            message.getLeaveApplication().setStatus(messageDTO.getStatus());
            message = messageRepository.save(message);
            MessageDTO messageDTO1 = convertMessageDTO(message);
            return new ResponseEntity(messageDTO1, HttpStatus.OK);
        }

        return new ResponseEntity("LEAVE_APPLICATION_NOT_EXITS", HttpStatus.BAD_REQUEST);
    }


/*    @PutMapping("/admin/readMessage")
    public ResponseEntity readMessage(){

    }*/

    @Autowired
    MessageImpl message;
    @GetMapping("/sendMessage")
    public RequestMessageDTO send(@RequestBody RequestMessageDTO requestMessageDTO, HttpServletRequest request){
        return message.requestMessageDTO(requestMessageDTO, request);
    }

    @GetMapping("/admin/messageUnread")
    public ResponseEntity messageUnread(HttpServletRequest request){
        return message.getAllMessageUnread(request);
    }
    @PostMapping("/admin/readAll")
    public ResponseEntity readAll(HttpServletRequest request){
        return message.readAll(request);
    }

}
