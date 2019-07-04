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

	//	trả về User có Id cần tìm, chuyển toàn bộ thông tin lên form Edit của Admin
	ResponseEntity getUserByIdToEditPage(int id);


}