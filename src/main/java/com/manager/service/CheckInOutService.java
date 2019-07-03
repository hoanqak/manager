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
}
