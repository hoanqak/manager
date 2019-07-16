package com.manager.controller;

import com.manager.common.Export2Excel;
import com.manager.data.Notifications;
import com.manager.dto.Checkin2Admin;
import com.manager.dto.PagedResponse;
import com.manager.dto.SignUpRequest;
import com.manager.dto.UserProfile2Admin;
import com.manager.model.TotalWorkingDay;
import com.manager.model.User;
import com.manager.service.AdminService;
import com.manager.service.CheckInOutService;
import com.manager.service.MessageService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/admin"})
public class AdminController {

	@Autowired
	AdminService adminService;
	@Autowired
	CheckInOutService checkInOutService;
	@Autowired
	MessageService messageService;
	@Autowired
	DozerBeanMapper mapper;

	@GetMapping("/users/")
	public PagedResponse<UserProfile2Admin> pageGetAllUser(@RequestParam("pageNumber") int pageNumber,
	                                                       @RequestParam("pageSize") int pageSize) {
		return adminService.pageGetAllUser(pageNumber, pageSize);
	}

	@PostMapping("/users/")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		User user = adminService.createUser(signUpRequest);
		if(user == null){
			return new ResponseEntity<>(Notifications.CREATE_USER_FAILED, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(Notifications.CREATE_USER_SUCCESS, HttpStatus.OK);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUserStatus(@PathVariable("id") int id,
	                                          @Valid @RequestBody UserProfile2Admin userProfile2Admin) {
		User user = adminService.updateUserStatus(id, userProfile2Admin);
		if(user == null){
			return new ResponseEntity<>(Notifications.UPDATE_USER_FAILED, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(Notifications.UPDATE_USER_SUCCESS, HttpStatus.OK);
	}

	@GetMapping("/users/{id}")
	public UserProfile2Admin getUserById(@PathVariable("id") int id) {
		return adminService.getUserByIdToEditPage(id);
	}
	@GetMapping("/users/checkInOuts/")
	public PagedResponse<Checkin2Admin> pageGetAllCheckInsAllUserByDate(@RequestParam("date") long date,
	                                                                    @RequestParam("pageNumber") int pageNumber,
	                                                                    @RequestParam("pageSize") int pageSize) {
		return checkInOutService.pageGetAllCheckInsAllUserByDate(date, pageNumber, pageSize);
	}
//----------------------------------------

	@GetMapping("/users/checkInOuts/user/")
	public ResponseEntity pageGetAllCheckinOfUser(@RequestParam("startDate") long startDate,
	                                              @RequestParam("endDate") long endDate,
	                                              @RequestParam(value = "pageNumber") int pageNumber,
	                                              @RequestParam("pageSize") int pageSize,
	                                              @RequestParam("idUser") int idUser) {
		return checkInOutService.getAllCheckInsOfUser(startDate, endDate, idUser, pageNumber, pageSize);
	}

	@GetMapping("/checkInOuts/")
	public ResponseEntity getACheckInById(@RequestParam("id") int id, HttpServletRequest request) {
		return checkInOutService.getACheckInById(id, request);
	}

	@GetMapping("/messageUnread/{page}/{size}")
	public ResponseEntity getAllMessageUnread(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request) {
		return messageService.getAllMessageUnreadPage(request, page, size);
	}

	// read message employee send, return a checkInOutDTO
	@PostMapping("/readMessage/{id}")
	public ResponseEntity readAMessage(@PathVariable("id") int id, HttpServletRequest request) {
		return messageService.readAMessage(id, request);
	}

//	//edit check in out of employee
//	@PostMapping("/updateCheckInOut")
//	public ResponseEntity updateCheckInOut(@RequestBody CheckInOutDTO checkInOutDTO) {
//		boolean check = checkInOutService.updateCheckInOut(checkInOutDTO);
//		return new ResponseEntity(check, HttpStatus.OK);
//	}

	@GetMapping("/checkInOuts/allMonth")
	public List<TotalWorkingDay> getTotalCheckinInMonth(@RequestParam("startDate") long startDate, @RequestParam("endDate") long endDate) {
		return adminService.getTotalCheckInInMonth(new Date(startDate), new Date(endDate));
	}

	@GetMapping("/checkInOuts/allMonth/exportToExcel")
	public ResponseEntity<?> export2Excel(@RequestParam("path") String path, @RequestParam("startDate") long startDate,
	                                   @RequestParam("endDate") long endDate) throws Exception {
		Export2Excel export2Excel = new Export2Excel();
		List<TotalWorkingDay> list = adminService.getTotalCheckInInMonth(new Date(startDate), new Date(endDate));
		export2Excel.writeExcel(list, path);
		return new ResponseEntity(Notifications.EXPORT_TO_FILE_EXCEL_SUCCESS, HttpStatus.OK);
	}


	@GetMapping("/messages/{page}/{size}")
	public ResponseEntity messages(@PathVariable("page") int page, @PathVariable("size") int size, HttpServletRequest request) {
		return messageService.messages(page, size, request);
	}

}
