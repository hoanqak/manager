package com.manager.service;

import com.manager.dto.LoginDTO;
import com.manager.dto.ProfileDTO;
import com.manager.dto.ResetPasswordDTO;
import com.manager.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public interface UserService {

    public ResponseEntity<String> login(LoginDTO loginDTO, HttpServletRequest request);

    public ResponseEntity<String> logOut(String tokenInHeader);

    public ResponseEntity<String> forgotPassword(LoginDTO loginDTO, HttpServletRequest request);

    public ResponseEntity<User> updateProfile(ProfileDTO profileDTO, String header);

    public ResponseEntity changePassword(ResetPasswordDTO resetPasswordDTO, String tokenInHeader);

    public ResponseEntity uploadFile(MultipartFile multipartFile, String tokenInHeader);

    public ResponseEntity<ProfileDTO> profile(String tokenInHeader);

    public ResponseEntity<String> resetPassword(ResetPasswordDTO resetPasswordDTO, String code, int id);
}
