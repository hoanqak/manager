package com.manager.controller;

import com.manager.dto.UserDTO;
import com.manager.model.User;
import com.manager.service.AdminService;
import com.manager.service.CheckInOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = {"/api/v1/admin", "/api/v1/manager"})
public class AdminController {

	@Autowired
	AdminService adminService;
	@Autowired
	CheckInOutService checkInOutService;

	@GetMapping("/users/")
	public ResponseEntity getAllUser(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
		return adminService.getAllUserInPage(pageNumber, pageSize);
	}

	@PostMapping("/users/")
	public ResponseEntity createUser(@RequestBody UserDTO userDTO) {
		return adminService.createUser(User.adminCreateUser(userDTO));
	}

	@PutMapping("/users/{id}")
	public ResponseEntity updateUserStatus(@PathVariable("id") int id, @RequestBody UserDTO userDTO) {

		return adminService.updateUserStatus(id, userDTO);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity getUserById(@PathVariable("id") int id) {
		return adminService.getUserByIdToEditPage(id);
	}

	@GetMapping("/users/checkInOuts/")
	public ResponseEntity pageGetAllCheckInsAllUserByDate(@RequestParam("date") long date, @RequestParam("pageNumber") int pageNumber,
	                                                      @RequestParam("pageSize") int pageSize) {
		return checkInOutService.pageGetAllCheckInsAllUserByDate(date, pageNumber, pageSize);
	}

	@GetMapping("/users/checkInOuts/user/")
	public ResponseEntity pageGetAllCheckinOfUser(@RequestParam("startDate") long startDate, @RequestParam("endDate") long endDate,
	                                              @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize,
	                                              @RequestParam("idUser") int idUser) {
		return checkInOutService.getAllCheckInsOfUser(startDate, endDate, idUser, pageNumber, pageSize);
	}

	@GetMapping("/checkInOuts/")
	public ResponseEntity getACheckInById(@RequestParam("id") int id, HttpServletRequest request) {
		return checkInOutService.getACheckInById(id, request);
	}
}
