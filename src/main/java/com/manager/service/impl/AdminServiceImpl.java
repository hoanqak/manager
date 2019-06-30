package com.manager.service.impl;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.UserDTO;
import com.manager.model.User;
import com.manager.repository.UserRepository;
import com.manager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	UserRepository userRepository;


	@Override
	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

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
		if(userByEmail != null){
			return new ResponseEntity("USER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
		}
		userRepository.save(user);
		return new ResponseEntity("CREATE_USER_SUCCESS", HttpStatus.OK);
	}

	@Override
	public ResponseEntity updateUser(User user) {
		userRepository.save(user);
		return new ResponseEntity("UPDATE_USER_SUCCESS", HttpStatus.OK);
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
