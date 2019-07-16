package com.manager.service;

import com.manager.dto.PagedResponse;
import com.manager.dto.UserProfile2Admin;
import com.manager.model.TotalWorkingDay;
import com.manager.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface AdminService {

	PagedResponse<UserProfile2Admin> pageGetAllUser(int pageNumber, int pageSize);

	User createUser(User user);

//	ResponseEntity updateUserStatus(int id, UserDTO userDTO);

	//	trả về User có Id cần tìm, chuyển toàn bộ thông tin lên form Edit của Admin
	UserProfile2Admin getUserByIdToEditPage(int id);

	List<TotalWorkingDay> getTotalCheckInInMonth(Date startDate, Date endDate);

}
