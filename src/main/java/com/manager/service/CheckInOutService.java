package com.manager.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CheckInOutService {
	ResponseEntity pageGetAllCheckInsAllUserByDate(long date, int pageNumber, int pageSize);

	ResponseEntity getAllCheckInsOfUser(long startDate, long endDate, int idUser, int pageNumber, int pageSize);

	ResponseEntity getACheckInById(int id);


}
