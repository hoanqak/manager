package com.manager.service.impl;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.UserDTO;
import com.manager.model.CheckInOut;
import com.manager.model.User;
import com.manager.repository.CheckInOutRepository;
import com.manager.repository.UserRepository;
import com.manager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	CheckInOutRepository checkInOutRepository;

	@Override
	public ResponseEntity getAllUserInPage(int pageNumber, int pageSize) {
//		phân trang trong data JPA.
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findUsersBy(pageable);

		List<User> userList = page.getContent();
		List<UserDTO> userDTOS = new ArrayList<>();
		for (User user : userList
		) {
			UserDTO userDTO = new UserDTO(user);
			userDTOS.add(userDTO);
		}
		return ResponseEntity.ok(userDTOS);
	}

	@Override
	public ResponseEntity createUser(User user) {
		User userByEmail = userRepository.findUserByEmail(user.getEmail());
		if (userByEmail != null) {
			return new ResponseEntity("USER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
		}
		userRepository.save(user);
		return new ResponseEntity("CREATE_USER_SUCCESS", HttpStatus.OK);
	}

	@Override
	public ResponseEntity updateUserStatus(int id, UserDTO userDTO) {
		User user = userRepository.findUserById(id);

		user.setStatus(userDTO.getStatus());
		user.setDepartment(userDTO.getDepartment());
		user.setPosition(userDTO.getPosition());
		user.setKindOfEmployee(userDTO.getKindOfEmployee());
		user.setRole(userDTO.getRole());
		user.setUpdatedDate(new Date(userDTO.getUpdatedDate()));

		userRepository.save(user);
		return new ResponseEntity("UPDATE_USER_SUCCESS", HttpStatus.OK);

	}

	@Override
	public ResponseEntity getUserByIdToEditPage(int id) {
		User user = userRepository.findUserById(id);
		if (user == null) return new ResponseEntity("USER_DOESNT_EXISTS", HttpStatus.OK);
		UserDTO userDTO = new UserDTO(user);
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity getHistoryCheckInOutByDate(long date, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Date date1 = new Date(date);
		Page<CheckInOut> page = checkInOutRepository.findCheckInOutsByDayCheckIn(pageable, date1);

		List<CheckInOut> checkInOuts = page.getContent();
		List<CheckInOutDTO> checkInOutDTOS = new ArrayList<>();
		for (CheckInOut checkInOut : checkInOuts) {
			checkInOutDTOS.add(new CheckInOutDTO(checkInOut));
		}
		return ResponseEntity.ok(checkInOutDTOS);
	}


	@Override
	public ResponseEntity getCheckInOutByTime(long startDate, long endDate, int pageNumber, int size) {
		return null;
	}

	@Override
	public ResponseEntity getCheckInOutOfUserByTime(String userId, long startDate, long endDate) {
		return null;
	}

	@Override
	public ResponseEntity updateCheckInOutOfUser(String userId, CheckInOutDTO checkInOutDTO) {
		return null;
	}

	@Override
	public ResponseEntity getLeaveApplicationOfUserByTime(String userId, long startDate, long endDate) {
		return null;
	}


}
