package com.manager.service;

import com.manager.dto.LeaveApplicationDTO;
import com.manager.dto.ListADayOffDTO;
import com.manager.dto.RequestADayOffDTO;
import com.manager.model.LeaveApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface LeaveApplicationService {
	ResponseEntity<RequestADayOffDTO> requestADayOff(RequestADayOffDTO requestADayOffDTO, HttpServletRequest request);

	ResponseEntity<List<LeaveApplication>> listDayOff(ListADayOffDTO listADayOffDTO, HttpServletRequest request);

	LeaveApplicationDTO convertToLeaveApplicationDTO(LeaveApplication leaveApplication);

	ResponseEntity<List<LeaveApplicationDTO>> getListApplicationDTO();
}
