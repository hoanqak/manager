package com.manager.service;

import com.manager.dto.CheckInOutDTO;
import com.manager.model.CheckInOut;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CheckInOutService {
    public ResponseEntity<String> checkIn(CheckInOutDTO checkInOutDTO, HttpServletRequest request);
    public ResponseEntity checkOut(CheckInOutDTO checkInOutDTO, HttpServletRequest request);
    public ResponseEntity<List<CheckInOut>> getListCheckInOut(HttpServletRequest request);

	ResponseEntity pageGetAllCheckInsAllUserByDate(long date, int pageNumber, int pageSize);

	ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize);

	ResponseEntity getACheckInById(int id);
}
