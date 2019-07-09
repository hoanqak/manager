package com.manager.service.impl;

import com.manager.data.Notifications;
import com.manager.dto.LeaveApplicationDTO;
import com.manager.dto.RequestADayOffDTO;
import com.manager.model.*;
import com.manager.repository.*;
import com.manager.service.LeaveApplicationService;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LeaveApplicationRepository leaveApplicationRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	MessageDemoRepository messageDemoRepository;
	@Autowired
	CheckInOutRepository checkInOutRepository;

	public ResponseEntity<RequestADayOffDTO> requestADayOff(LeaveApplicationDTO leaveApplicationDTO, HttpServletRequest request) {
		String codeToken = request.getHeader("access_Token");
		if (codeToken == null) {
			return new ResponseEntity(Notifications.NOT_LOGGED_IN, HttpStatus.BAD_REQUEST);
		}
		Token token = tokenRepository.getTokenByCode(codeToken);
		User user = userRepository.getUserById(token.getId());
		if (user != null) {
			Calendar calendarFromDate = Calendar.getInstance();
			Calendar calendarToDate = Calendar.getInstance();

			calendarFromDate.setTimeInMillis(leaveApplicationDTO.getStartTime());
			calendarToDate.setTimeInMillis(leaveApplicationDTO.getEndTime());

			LeaveApplication leaveApplication = new LeaveApplication();
			leaveApplication.setStartTime(calendarFromDate.getTime());
			leaveApplication.setEndTime(calendarToDate.getTime());
			leaveApplication.setReason(leaveApplicationDTO.getReason());
			leaveApplication.setStatus("pending");
			leaveApplication.setUser(user);

			leaveApplication = leaveApplicationRepository.save(leaveApplication);
			MessageDemo messageDemo = new MessageDemo();
			messageDemo.setIdReport(leaveApplication.getId());
			messageDemo.setTitle("User: " + user.getId() + "(" + user.getName() + ") request a day off");
			messageDemo.setStatus(false);
			messageDemo.setFrom(user);
			//type = 1: leave application
			// type = 0: edit checkinout
			messageDemo.setType(1);

			//send to all manager(role 2)
			userRepository.getRoleUser(2).forEach(user1 -> {
				messageDemo.setTo(user1);
				messageDemoRepository.save(messageDemo);
			});
			return new ResponseEntity(leaveApplicationDTO, HttpStatus.OK);
		}
		return new ResponseEntity(Notifications.ERROR_REQUEST, HttpStatus.OK);
	}

	public ResponseEntity<List<LeaveApplication>> listDayOff(int month, HttpServletRequest request) {
		String code = request.getHeader("access_Token");
		if (code == null) {
			return new ResponseEntity(Notifications.NOT_LOGGED_IN, HttpStatus.BAD_REQUEST);
		}
		Token token1 = tokenRepository.getTokenByCode(code);
		int idUser = token1.getId();
		List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getListApplicationInWeek(month, idUser);
		List<LeaveApplicationDTO> leaveApplicationDTOS = new LinkedList<>();
		leaveApplicationList.forEach(leaveApplication -> {
			BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
				@Override
				protected void configure() {
					mapping(LeaveApplication.class, LeaveApplicationDTO.class).fields("startTime", "fromDate").fields("endTime", "toDate");
				}
			};
			DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
			dozerBeanMapper.addMapping(beanMappingBuilder);
			LeaveApplicationDTO leaveApplicationDTO1 = dozerBeanMapper.map(leaveApplication, LeaveApplicationDTO.class);
			leaveApplicationDTOS.add(leaveApplicationDTO1);

		});
		return new ResponseEntity(leaveApplicationDTOS, HttpStatus.OK);
	}
	public LeaveApplicationDTO convertToLeaveApplicationDTO(LeaveApplication leaveApplication) {
		LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
		leaveApplicationDTO.setId(leaveApplication.getId());
		leaveApplicationDTO.setName(leaveApplication.getUser().getName());
		leaveApplicationDTO.setReason(leaveApplication.getReason());
		leaveApplicationDTO.setStatus(leaveApplication.getStatus());
		long fromDate = leaveApplication.getStartTime().getTime();
		long toDate = leaveApplication.getEndTime().getTime();
		leaveApplicationDTO.setStartTime(fromDate);
		leaveApplicationDTO.setEndTime(toDate);
		try {
			leaveApplicationDTO.setPosition(Details.positions[leaveApplication.getUser().getPosition()]);
		}catch (ArrayIndexOutOfBoundsException arrEx){
			arrEx.printStackTrace();
		}
		return leaveApplicationDTO;
	}

	public ResponseEntity<List<LeaveApplicationDTO>> getListApplicationDTO() {
		List<LeaveApplicationDTO> leaveApplicationDTOS = new LinkedList<LeaveApplicationDTO>();
		for (LeaveApplication leaveApplication : leaveApplicationRepository.findAll()) {
			LeaveApplicationDTO leaveApplicationDTO = convertToLeaveApplicationDTO(leaveApplication);
			leaveApplicationDTOS.add(leaveApplicationDTO);
		}
		return new ResponseEntity<List<LeaveApplicationDTO>>(leaveApplicationDTOS, HttpStatus.OK);
	}

	public ResponseEntity listDayOffPage(int page, int size, HttpServletRequest request) {
		String code = request.getHeader("access_Token");
		Token token = tokenRepository.getTokenByCode(code);
		User user = userRepository.getUserById(token.getId());
		Pageable pageable = PageRequest.of(page, size);
		Page<LeaveApplication> leaveApplicationPage = leaveApplicationRepository.getLeaveApplicationByPage(pageable, user);

		List<LeaveApplicationDTO> leaveApplicationDTOS = new LinkedList<>();
		leaveApplicationPage.getContent().forEach(leaveApplication -> {
			LeaveApplicationDTO leaveApplicationDTO = convertToLeaveApplicationDTO(leaveApplication);
			leaveApplicationDTOS.add(leaveApplicationDTO);
		});

		return new ResponseEntity(leaveApplicationDTOS, HttpStatus.OK);
	}
}
