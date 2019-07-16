package com.manager.service;

import com.manager.dto.Checkin2Admin;
import com.manager.dto.PagedResponse;
import com.manager.model.CheckInOut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
public interface CheckInOutService {
//	ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO, HttpServletRequest request);
//
//	ResponseEntity checkOut(CheckInOutDTO checkInOutDTO, HttpServletRequest request);

	ResponseEntity<List<CheckInOut>> getListCheckInOut(HttpServletRequest request);

	PagedResponse<Checkin2Admin> pageGetAllCheckInsAllUserByDate(long date, int pageNumber, int pageSize);

	ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize);

	ResponseEntity getACheckInById(int id, HttpServletRequest request);

	ResponseEntity getCheckInOutAndPage(int page, int size, HttpServletRequest request);

//	boolean updateCheckInOut(CheckInOutDTO checkInOutDTO);

	ResponseEntity getACheckInById(int id);


}
