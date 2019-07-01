package com.manager.service;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.LoginDTO;
import com.manager.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    public ResponseEntity<String> login(LoginDTO loginDTO, HttpServletRequest request);

    public ResponseEntity<String> logOut(HttpServletRequest request);

    public ResponseEntity<String> forgotPassword(LoginDTO loginDTO, HttpServletRequest request);

    public ResponseEntity<User> editProfile(int id);

    public ResponseEntity requestADayOff(Date fromDate, Date toDate, String reason);

}
