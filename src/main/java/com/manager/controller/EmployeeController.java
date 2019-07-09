package com.manager.controller;

import com.manager.dto.*;
import com.manager.model.*;
import com.manager.repository.*;
import com.manager.service.CheckInOutService;
import com.manager.service.Impl.LeaveApplicationServiceImpl;
import com.manager.service.Impl.MessageServiceImpl;
import com.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController{

	@Autowired
	private UserService userService;

	@Autowired
	private LeaveApplicationServiceImpl leaveApplicationService;

	@Autowired
	private CheckInOutService checkInOutService;
	@Autowired
	MessageServiceImpl message;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO,
	                                    HttpServletRequest request) {
		return userService.login(loginDTO, request);
	}

	@PostMapping("/logout")
	public ResponseEntity logout(HttpServletRequest request) {
		return userService.logOut(request);
	}

	//get profile
	@GetMapping("/profile")
	public ResponseEntity<ProfileDTO> profile(HttpServletRequest request) {
		return userService.profile(request);
	}

	//update profile
	@PostMapping("/profile")
	public ResponseEntity updateProfile(@RequestBody ProfileDTO profileDTO, HttpServletRequest request) {
		return userService.updateProfile(profileDTO, request);
	}

	@PostMapping("/uploadAvatar")
	public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
		return userService.uploadFile(multipartFile, request);
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
	public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {
		return userService.changePassword(resetPasswordDTO, request);
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
	public ResponseEntity test(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request){
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
	public ResponseEntity readMessage(@PathVariable("id") int id, HttpServletRequest request){
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
	public ResponseEntity listDayOffPage(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request){
		return leaveApplicationService.listDayOffPage(page, size, request);
	}
	@GetMapping("/messages/{page}/{size}")
	public ResponseEntity messages(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request){
		return message.messages(page, size, request);
	}


}
