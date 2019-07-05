package com.manager.service;

import com.manager.dto.MessageDemoDTO;
import com.manager.dto.RequestMessageDTO;
import com.manager.model.MessageDemo;
import com.manager.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
public interface MessageService {
	User getUser(HttpServletRequest request);

	RequestMessageDTO requestEditCheckInOut(RequestMessageDTO requestMessageDTO, HttpServletRequest request);

	ResponseEntity<List<MessageDemoDTO>> getAllMessageUnread(HttpServletRequest request, int type);

	MessageDemoDTO convertToMessageDemoDTO(MessageDemo messageDemo);

	ResponseEntity readAll(HttpServletRequest request);

}
