package com.manager.controller;

import com.manager.dto.UserDTO;
import com.manager.model.User;
import com.manager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/admin", "/api/v1/manager"})
public class AdminController {

	@Autowired
	AdminService adminService;


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
	public ResponseEntity getHistoryCheckInOutByDate(@RequestParam("date") long date, @RequestParam("pageNumber") int pageNumber,
	                                                 @RequestParam("pageSize") int pageSize) {
		return adminService.getHistoryCheckInOutByDate(date, pageNumber, pageSize);
	}


}
