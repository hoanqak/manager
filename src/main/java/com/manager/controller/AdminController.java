package com.manager.controller;

import com.manager.config.WriteExcel;
import com.manager.dto.UserDTO;
import com.manager.model.TotalWorkingDay;
import com.manager.model.User;
import com.manager.service.AdminService;
import com.manager.service.CheckInOutService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/admin", "/api/v1/manager"})
public class AdminController {

	@Autowired
	AdminService adminService;
	@Autowired
	CheckInOutService checkInOutService;

	DozerBeanMapper mapper;

	@GetMapping("/users/")
	public ResponseEntity getAllUser(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
		return adminService.getAllUserInPage(pageNumber, pageSize);
	}

	@PostMapping("/users/")
	public ResponseEntity createUser(@RequestBody UserDTO userDTO) {

		DozerBeanMapper mapper = new DozerBeanMapper();
		User user = mapper.map(userDTO, User.class);
		return adminService.createUser(user);
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
	public ResponseEntity getACheckInById(@RequestParam("id") int id) {
		return checkInOutService.getACheckInById(id);
	}

	@GetMapping("/checkInOuts/allMonth")
	public List<TotalWorkingDay> getTotalCheckinInMonth(@RequestParam("startDate") long startDate, @RequestParam("endDate") long endDate){
		return adminService.getTotalCheckInInMonth(new Date(startDate), new Date(endDate));
	}

	@GetMapping("/checkInOuts/allMonth/exportToExcel")
	public ResponseEntity export2Excel(@RequestParam("path") String path, @RequestParam("startDate") long startDate,
	                                   @RequestParam("endDate") long endDate) throws Exception {
		WriteExcel writeExcel = new WriteExcel();
		List<TotalWorkingDay> list = adminService.getTotalCheckInInMonth(new Date(startDate), new Date(endDate));
		writeExcel.writeExcel(list, path);
		return new ResponseEntity("EXPORT_FILE_SUCCESS", HttpStatus.OK);
	}



//	@Autowired
//	CheckInOutRepository check;
//
//	@GetMapping("/checkInOuts/all")
//	public List<CheckInOutDTO> getAll() {
//		mapper = new DozerBeanMapper();
//		List<CheckInOutDTO> checkInOutDTOS = new ArrayList<>();
//		check.findAll().forEach(checkInOut ->
//			checkInOutDTOS.add(mapper.map(checkInOut, CheckInOutDTO.class)));
//		return checkInOutDTOS;
//	}

}
