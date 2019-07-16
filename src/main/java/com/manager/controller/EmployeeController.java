package com.manager.controller;

import com.manager.dto.*;
import com.manager.model.LeaveApplication;
import com.manager.service.CheckInOutService;
import com.manager.service.LeaveApplicationService;
import com.manager.service.MessageService;
import com.manager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(value="CONTROLLER EMPLOYEE")
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired
	UserService userService;

	@Autowired
	LeaveApplicationService leaveApplicationService;

	@Autowired
	CheckInOutService checkInOutService;

	@Qualifier("messageServiceImpl")
	@Autowired
	MessageService message;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO, BindingResult result,
	                                    HttpServletRequest request) {
		return userService.login(loginDTO, result, request);
	}

	@PostMapping("/logout")
	public ResponseEntity logout(@RequestHeader String token) {
		return userService.logOut(token);
	}

	//get profile
	@GetMapping("/profile")
	public ResponseEntity<ProfileDTO> profile(@RequestHeader String token) {
		return userService.profile(token);
	}

	//update profile
	@PostMapping("/profile")
	public ResponseEntity updateProfile(@RequestBody ProfileDTO profileDTO, @RequestHeader String token) {
		return userService.updateProfile(profileDTO, token);
	}

	@PostMapping("/uploadAvatar")
	public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestHeader String token) {
		return userService.uploadFile(multipartFile, token);
	}

	//Reset Password, send mail
	@PostMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
		return userService.forgotPassword(loginDTO, request);
	}

	//reset password in mail when request forgot password
	@GetMapping("/forgotPassword/{code}/{id}")
	public ResponseEntity<String> resetPasswordInMail(@RequestBody ResetPasswordDTO resetPasswordDTO, @PathVariable("code") String code, @PathVariable("id") int id) {
		return userService.resetPassword(resetPasswordDTO, code, id);
	}

	//Change password in profile
	@PutMapping("/changePassword")
	public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, @RequestHeader String token) {
		return userService.changePassword(resetPasswordDTO, token);
	}

	// check in
	@PostMapping("/checkIn")
	public ResponseEntity<String> checkIn(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		return checkInOutService.checkIn(checkInOutDTO, request);
	}

	// check out
	@PostMapping("/checkOut")
	public ResponseEntity CheckOut(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		return checkInOutService.checkOut(checkInOutDTO, request);
	}

/*	//get list checkInOuts
	@GetMapping("/checkInOuts")
	public ResponseEntity<List<CheckInOut>> checkInOuts(HttpServletRequest request) {
		return checkInOutService.getListCheckInOut(request);
	}*/

	//get all list check in check out
	@GetMapping("/checkInOuts/{page}/{size}")
	public ResponseEntity test(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request) {
		return checkInOutService.getCheckInOutAndPage(page, size, request);
	}

	// request a edit check in or check out to admin
	@PostMapping("/requestEditCheckInOut")
	public RequestMessageDTO requestEditCheckInOut(@RequestBody RequestMessageDTO requestMessageDTO, HttpServletRequest request) {
		return message.requestEditCheckInOut(requestMessageDTO, request);
	}

	// get list message unread by page and size
	@GetMapping("/messageUnread/{page}/{size}")
	public ResponseEntity<List<MessageDemoDTO>> getMessageUnread(HttpServletRequest request, @PathVariable("page") int page, @PathVariable("size") int size) {
		return message.getAllMessageUnreadPage(request, page, size);
	}

	//read a message
	@PostMapping("/readMessage/{id}")
	public ResponseEntity readMessage(@PathVariable("id") int id, HttpServletRequest request) {
		return message.readAMessage(id, request);
	}

	// create leave application send to manager
	@PostMapping("/requestADayOff")
	public ResponseEntity requestADayOff(@RequestBody LeaveApplicationDTO leaveApplicationDTO, HttpServletRequest request) {
		return leaveApplicationService.requestADayOff(leaveApplicationDTO, request);
	}

	//get leave application by month
	@GetMapping("/listDayOff/{month}")
	public ResponseEntity<List<LeaveApplication>> listDayOff(@PathVariable("month") int month, HttpServletRequest request) {
		return leaveApplicationService.listDayOff(month, request);
	}

	//get list leave application with page and size
	@GetMapping("/listDayOff/{page}/{size}")
	public ResponseEntity listDayOffPage(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request) {
		return leaveApplicationService.listDayOffPage(page, size, request);
	}

	@GetMapping("/messages/{page}/{size}")
	public ResponseEntity messages(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request) {
		return message.messages(page, size, request);
	}


}
