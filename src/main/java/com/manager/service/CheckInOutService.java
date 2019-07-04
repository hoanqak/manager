package com.manager.service;

import com.manager.dto.CheckInOutDTO;
import com.manager.model.CheckInOut;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CheckInOutService {
	ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO, HttpServletRequest request);

	ResponseEntity checkOut(CheckInOutDTO checkInOutDTO, HttpServletRequest request);

	ResponseEntity<List<CheckInOut>> getListCheckInOut(HttpServletRequest request);

	ResponseEntity pageGetAllCheckInsAllUserByDate(long date, int pageNumber, int pageSize);

	ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize);

	ResponseEntity getACheckInById(int id);
}
