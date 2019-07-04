package com.manager.controller;

import com.manager.dto.LeaveApplicationDTO;
import com.manager.dto.MessageDemoDTO;
import com.manager.service.Impl.LeaveApplicationServiceImpl;
import com.manager.service.Impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/")
public class ManagerController {


	@Autowired
	private MessageServiceImpl message;
	@Autowired
	private LeaveApplicationServiceImpl leaveApplicationService;

	@GetMapping("/messageUnread")
	public ResponseEntity<List<MessageDemoDTO>> getMessageUnread(HttpServletRequest request) {
		return message.getAllMessageUnread(request);
	}

	@PostMapping("/readAll")
	public ResponseEntity readMessage(HttpServletRequest request) {
		return message.readAll(request);
	}

	@PostMapping("/process")
	public ResponseEntity processLeaveApplication() {
		return null;
	}

	@GetMapping("/leaveApplications")
	public ResponseEntity<List<LeaveApplicationDTO>> getAllListLeaveApplication() {
		return leaveApplicationService.getListApplicationDTO();
	}

}
