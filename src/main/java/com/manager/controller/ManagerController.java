package com.manager.controller;

import com.manager.repository.MessageDemoRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/manager/")
@Transactional
public class ManagerController {


	@Qualifier("messageServiceImpl")
	@Autowired
	MessageService message;
	@Autowired
	TokenRepository tokenRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageDemoRepository messageDemoRepository;

	//get all message by page
	@GetMapping("/messageUnread/{page}/{size}")
	public ResponseEntity getMessageUnreadPage(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request) {
		return message.getAllMessageUnreadPage(request, page, size);
	}

	//read a message
	@GetMapping("/readMessage/{id}")
	public ResponseEntity readAMessage(@PathVariable("id") int id, HttpServletRequest request) {
		return message.readAMessage(id, request);
	}

	//process a message, process leave application
	@PostMapping("/process/{idMessage}/{action}")
	public ResponseEntity processMessage(@PathVariable("idMessage") int id, @PathVariable("action") int accept, HttpServletRequest request) {
		return message.processLeaveApplication(id, accept, request);
	}

	@GetMapping("/messages/{page}/{size}")
	public ResponseEntity messages(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request) {
		return message.messages(page, size, request);
	}

   /* @GetMapping("/leaveApplications")
    public ResponseEntity<List<LeaveApplicationDTO>> getAllListLeaveApplication() {
        return leaveApplicationService.getListApplicationDTO();
    }
*/

}
