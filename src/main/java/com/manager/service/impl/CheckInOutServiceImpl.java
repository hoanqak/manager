package com.manager.service.impl;

import com.manager.dto.CheckInOutDTO;
import com.manager.model.CheckInOut;
import com.manager.repository.CheckInOutRepository;
import com.manager.service.CheckInOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CheckInOutServiceImpl implements CheckInOutService {

	@Autowired
	CheckInOutRepository checkInOutRepository;

	@Override
	public ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		return null;
	}

	@Override
	public ResponseEntity checkOut(CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
		return null;
	}

	@Override
	public ResponseEntity<List<CheckInOut>> getListCheckInOut(HttpServletRequest request) {
		return null;
	}

	@SuppressWarnings("Duplicates")
	@Override
	public ResponseEntity pageGetAllCheckInsAllUserByDate(long date, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<CheckInOut> page = checkInOutRepository.findCheckInOutsByDayCheckIn(pageable, new Date(date));
		List<CheckInOut> checkInOuts = page.getContent();
		List<CheckInOutDTO> checkInOutDTOS = new ArrayList<>();
		for (CheckInOut checkInOut : checkInOuts) {
			checkInOutDTOS.add(new CheckInOutDTO(checkInOut));
		}
		return new ResponseEntity(checkInOutDTOS, HttpStatus.OK);
	}

	@SuppressWarnings("Duplicates")
	@Override
	public ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<CheckInOut> page = checkInOutRepository.findCheckInOutsByDayCheckInAndAndUserId(pageable, new Date(startDate), new Date(endDate), idUser);
		List<CheckInOut> checkInOuts = page.getContent();
		List<CheckInOutDTO> checkInOutDTOS = new ArrayList<>();
		for (CheckInOut checkInOut : checkInOuts) {
			checkInOutDTOS.add(new CheckInOutDTO(checkInOut));
		}
		return new ResponseEntity(checkInOutDTOS, HttpStatus.OK);
	}

	@Override
	public ResponseEntity getACheckInById(int id) {

		return new ResponseEntity(new CheckInOutDTO(checkInOutRepository.getOne(id)), HttpStatus.OK);

	}
}
