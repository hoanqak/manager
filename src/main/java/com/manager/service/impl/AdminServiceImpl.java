package com.manager.service.impl;

import com.manager.dto.UserDTO;
import com.manager.model.CheckInOut;
import com.manager.model.TotalWorkingDay;
import com.manager.model.User;
import com.manager.repository.CheckInOutRepository;
import com.manager.repository.UserRepository;
import com.manager.service.AdminService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	CheckInOutRepository checkInOutRepository;

	//	mapping model
	DozerBeanMapper mapper = new DozerBeanMapper();


	@Override
	public ResponseEntity getAllUserInPage(int pageNumber, int pageSize) {
//		phân trang trong data JPA.
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findUsersBy(pageable);

		List<User> userList = page.getContent();
		List<UserDTO> userDTOS = new ArrayList<>();

//		forEach() Java 8
		userList.forEach(user -> {
			UserDTO userDTO = mapper.map(user, UserDTO.class);
			userDTOS.add(userDTO);

		});

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

		return new ResponseEntity("UPDATE_USER_SUCCESS", HttpStatus.OK);

	}

	@Override
	public ResponseEntity getUserByIdToEditPage(int id) {
		User user = userRepository.findUserById(id);
		if (user == null) return new ResponseEntity("USER_DOESNT_EXISTS", HttpStatus.OK);

		UserDTO userDTO = mapper.map(user, UserDTO.class);
		return new ResponseEntity(userDTO, HttpStatus.OK);
	}

	@Override
	public List<TotalWorkingDay> getTotalCheckInInMonth(Date startDate, Date endDate) {
		List<TotalWorkingDay> list = new ArrayList<>();
		List<CheckInOut> checkInOuts;
//		List lấy ra toàn bộ danh sách nhân viên trong công ty.
		List<User> users = userRepository.findAll();

		for (User user : users) {
			checkInOuts = checkInOutRepository.getListCheckInOutsByDayCheckInAndUserId(startDate, endDate, user.getId());
			TotalWorkingDay workingDayDTO = new TotalWorkingDay();
			workingDayDTO.setUserId(user.getId());
			workingDayDTO.setName(user.getName());
			workingDayDTO.setPosition(user.getPosition());
//			workingDayDTO.setTotal();
//			chuyển từ List sang Map trong Java 8.
//			Map<Date, Integer> days = checkInOuts.stream().collect(Collectors.toMap(CheckInOut::getDayCheckIn, CheckInOut::getTotalTime));
			Map<String, Integer> days = new HashMap<>();
			for (CheckInOut checkInOut : checkInOuts) {
				days.put(date2String(checkInOut.getDayCheckIn()), checkInOut.getTotalTime());
			}
			double total = 0;
			for (CheckInOut checkInOut : checkInOuts) {
				total += checkInOut.getTotalTime();
			}
			total = total / 8;
			workingDayDTO.setTotal(total);
			workingDayDTO.setDays(days);
			list.add(workingDayDTO);
		}
		return list;
	}

	public String date2String(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1);
	}
}