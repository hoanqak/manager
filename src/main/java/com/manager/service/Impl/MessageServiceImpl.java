package com.manager.service.Impl;

import com.manager.dto.MessageDemoDTO;
import com.manager.dto.RequestMessageDTO;
import com.manager.model.MessageDemo;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.MessageDemoRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
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

	public User getUser(HttpServletRequest request) {
		String code = request.getHeader("access_Token");
		Token token = tokenRepository.getTokenByCode(code);
		User user = userRepository.getUserById(token.getId());
		return user;
	}

	public RequestMessageDTO requestEditCheckInOut(RequestMessageDTO requestMessageDTO, HttpServletRequest request) {
		User user = getUser(request);
		if (requestMessageDTO.getType() == 0) {
			MessageDemo messageDemo = new MessageDemo();
			messageDemo.setTitle("ID: " + "(" + user.getName() + ")" + " Request edit checkin time");
			messageDemo.setContent(requestMessageDTO.getContent());
			messageDemo.setFrom(user);
			messageDemo.setType(0);
			messageDemo.setIdReport(requestMessageDTO.getIdCheckInOut());
			userRepository.getRoleUser(2).forEach(user1 ->{
				messageDemo.setTo(user1);
				messageDemoRepository.save(messageDemo);
			});
		} else if (requestMessageDTO.getType() == 1) {
			MessageDemo messageDemo = new MessageDemo();
			messageDemo.setTitle("ID: " + user.getId() + "(" + user.getName() + ")" + " Request edit checkOut time");
			messageDemo.setContent(requestMessageDTO.getContent());
			messageDemo.setFrom(user);
			messageDemo.setType(0);
			messageDemo.setIdReport(requestMessageDTO.getIdCheckInOut());
			for (User user1 : userRepository.getRoleUser(2)) {
				messageDemo.setTo(user1);
				messageDemoRepository.save(messageDemo);
			}
		}
		return requestMessageDTO;
	}

	public ResponseEntity<List<MessageDemoDTO>> getAllMessageUnread(HttpServletRequest request) {
		User user = getUser(request);
		List<MessageDemoDTO> messageDemoDTOList = new LinkedList<MessageDemoDTO>();
		for (MessageDemo messageDemo : messageDemoRepository.getAllMessageByStatusAndTo(false, user)) {
			MessageDemoDTO messageDemoDTO = convertToMessageDemoDTO(messageDemo);
			messageDemoDTOList.add(messageDemoDTO);
		}
		return new ResponseEntity<>(messageDemoDTOList, HttpStatus.OK);

	}

	public MessageDemoDTO convertToMessageDemoDTO(MessageDemo messageDemo) {
		MessageDemoDTO messageDemoDTO = new MessageDemoDTO();
		messageDemoDTO.setContent(messageDemo.getContent());
		messageDemoDTO.setMessage(messageDemo.getTitle());
		messageDemoDTO.setTo(messageDemo.getTo().getName());
		messageDemoDTO.setFrom(messageDemo.getFrom().getName());
		if(messageDemo.getType() == 0){
			messageDemoDTO.setType("REQUEST_EDIT_CHECKIN");
		}else if(messageDemo.getType() == 1){
			messageDemoDTO.setType("REQUEST_A_DAY_OFF");
		}else if(messageDemo.getType() == -1){
			messageDemoDTO.setType("REQUEST_EDIT_CHECKOUT");
		}
		messageDemoDTO.setId(messageDemo.getId());
		long time = messageDemo.getTimeRequest().getTime();
		messageDemoDTO.setTimeRequest(time);

		return messageDemoDTO;
	}

	public ResponseEntity readAll(HttpServletRequest request) {
		User user = getUser(request);
		for (MessageDemo messageDemo : messageDemoRepository.getAllMessageByStatusAndTo(false, user)) {
			messageDemo.setStatus(true);
		}
		return new ResponseEntity(true, HttpStatus.OK);
	}
}
