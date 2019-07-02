package com.manager.service;

import com.manager.dto.LoginDTO;
import com.manager.dto.ProfileDTO;
import com.manager.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    public ResponseEntity<String> login(LoginDTO loginDTO, HttpServletRequest request);

    public ResponseEntity<String> logOut(HttpServletRequest request);

    public ResponseEntity<String> forgotPassword(LoginDTO loginDTO, HttpServletRequest request);

    public ResponseEntity<User> updateProfile(ProfileDTO profileDTO, HttpServletRequest request);

}
