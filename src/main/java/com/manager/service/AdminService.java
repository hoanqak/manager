package com.manager.service;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.UserDTO;
import com.manager.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.List;

@Service
public interface AdminService {

	User findUserByEmail(String email);

	ResponseEntity getAllUserInPage(int pageNumber, int pageSize);

	ResponseEntity createUser(User user);

	ResponseEntity updateUser(User user);

	ResponseEntity getCheckInOutByTime(long startDate, long endDate, int pageNumber, int size);

	ResponseEntity getCheckInOutOfUserByTime(String userId, long startDate, long endDate);

	ResponseEntity updateCheckInOutOfUser(String userId, CheckInOutDTO checkInOutDTO);

	ResponseEntity getLeaveApplicationOfUserByTime(String userId, long startDate, long endDate);
}
