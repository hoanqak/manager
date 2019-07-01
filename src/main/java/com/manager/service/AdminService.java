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

	ResponseEntity getAllUserInPage(int pageNumber, int pageSize);

	ResponseEntity createUser(User user);

	ResponseEntity updateUserStatus(int id, UserDTO userDTO);

	ResponseEntity getCheckInOutByTime(long startDate, long endDate, int pageNumber, int size);

	ResponseEntity getCheckInOutOfUserByTime(String userId, long startDate, long endDate);

	ResponseEntity updateCheckInOutOfUser(String userId, CheckInOutDTO checkInOutDTO);

	ResponseEntity getLeaveApplicationOfUserByTime(String userId, long startDate, long endDate);

	//	trả về User có Id cần tìm, chuyển toàn bộ thông tin lên form Edit của Admin
	ResponseEntity getUserByIdToEditPage(int id);

	ResponseEntity getHistoryCheckInOutByDate(long date, int pageNumber, int pageSize);

}
