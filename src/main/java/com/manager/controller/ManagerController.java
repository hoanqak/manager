package com.manager.controller;

import com.manager.dto.LeaveApplicationDTO;
import com.manager.dto.MessageDemoDTO;
import com.manager.model.LeaveApplication;
import com.manager.model.MessageDemo;
import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.LeaveApplicationRepository;
import com.manager.repository.MessageDemoRepository;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import com.manager.service.Impl.LeaveApplicationServiceImpl;
import com.manager.service.Impl.MessageServiceImpl;
import com.manager.service.LeaveApplicationService;
import com.manager.service.MessageService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/")
@Transactional
public class ManagerController {


	@Autowired
	private MessageServiceImpl message;
	@Autowired
	private LeaveApplicationService leaveApplicationService;
	@Autowired
	private LeaveApplicationRepository leaveApplicationRepository;
	@Autowired
	TokenRepository tokenRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageDemoRepository messageDemoRepository;
	@GetMapping("/messageUnread")
	public ResponseEntity<List<MessageDemoDTO>> getMessageUnread(HttpServletRequest request) {
		return message.getAllMessageUnread(request, 1);
	}

	@GetMapping("/messageUnread/{page}/{size}")
	public ResponseEntity getMessageUnreadPage(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request){
		return message.getAllMessageUnreadPage(request, page, size);
	}

	@PostMapping("/readAll")
	public ResponseEntity readMessage(HttpServletRequest request) {
		return message.readAll(request);
	}

	/*@PostMapping("/process/{idLeaveApplication}/{accept}")
	public ResponseEntity processLeaveApplication(@PathVariable("idLeaveApplication") int id, @PathVariable("accept") int accept, HttpServletRequest request) {
		String code = request.getHeader("access_Token");
		Token token = tokenRepository.getTokenByCode(code);
		User user = userRepository.getUserById(token.getId());
		LeaveApplication leaveApplication = leaveApplicationRepository.getLeaveApplicationsById(id);
		if(leaveApplication != null){
			if(accept >= 1){
				System.out.println("vao day roi");
				User userTo = leaveApplication.getUser();
				System.out.println(userTo.getName());
				leaveApplication.setStatus("accept");
				MessageDemo messageDemo = new MessageDemo();
				messageDemo.setFrom(user);
				messageDemo.setTitle(user.getName() +" da chap nhan don xin nghi phep");
				messageDemo.setTo(userTo);
				messageDemo.setIdReport(leaveApplication.getId());
				messageDemoRepository.save(messageDemo);
				return new ResponseEntity("ACCEPT", HttpStatus.OK);
			}else{
				leaveApplication.setStatus("not accept");
				User userTo = leaveApplication.getUser();
				System.out.println(userTo.getName());
				MessageDemo messageDemo = new MessageDemo();
				messageDemo.setFrom(user);
				messageDemo.setTitle(user.getName() +" khong chap nhan don xin nghi phep");
				messageDemo.setTo(userTo);
				messageDemo.setIdReport(leaveApplication.getId());
				messageDemoRepository.save(messageDemo);
				return new ResponseEntity("NOT_ACCEPT", HttpStatus.OK);
			}
		}
		return new ResponseEntity("MESSAGE_NOT_EXITS", HttpStatus.OK);
	}
*/

	@GetMapping("/process/{idMessage}/{accept}")
	public ResponseEntity processMessage(@PathVariable("idMessage") int id, @PathVariable("accept") int accept, HttpServletRequest request){
		MessageDemo messageDemo =messageDemoRepository.getMessageDemoById(id);
		if(messageDemo == null){
			return new ResponseEntity("MESSAGE_NOT_EXITS", HttpStatus.BAD_REQUEST);
		}

		int idLeaveApplication = messageDemo.getIdReport();
		LeaveApplication leaveApplication = leaveApplicationRepository.getLeaveApplicationsById(idLeaveApplication);
		if(leaveApplication == null){
			return new ResponseEntity("LEAVE_APPLICATION_NOT_EXITS", HttpStatus.BAD_REQUEST);
		}
		//reply result to sender
		String code = request.getHeader("access_Token");
		Token token = tokenRepository.getTokenByCode(code);
		User user = userRepository.getUserById(token.getId());
		MessageDemo messageReply = new MessageDemo();
		messageReply.setFrom(user);
		messageReply.setTo(messageDemo.getFrom());
		if(accept >= 1) {
			messageReply.setTitle(user.getName() + " accepted leave application of me");
			leaveApplication.setStatus("accept");

		}else{
			messageReply.setTitle(user.getName() + " not accepted leave application of me");
			leaveApplication.setStatus("not accept");
		}
		messageReply.setStatus(false);
		messageReply.setIdReport(idLeaveApplication);
		messageReply.setType(1);
		messageDemo.setStatus(true);
		messageDemoRepository.save(messageDemo);
		messageDemoRepository.save(messageReply);
		leaveApplicationRepository.save(leaveApplication);
		LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationServiceImpl().convertToLeaveApplicationDTO(leaveApplication);
		return new ResponseEntity(leaveApplicationDTO, HttpStatus.OK);
	}
	@GetMapping("/leaveApplications")
	public ResponseEntity<List<LeaveApplicationDTO>> getAllListLeaveApplication() {
		return leaveApplicationService.getListApplicationDTO();
	}

}
