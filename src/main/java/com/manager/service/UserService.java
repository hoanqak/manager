package com.manager.service;

import com.manager.dto.LoginDTO;
import com.manager.dto.ProfileDTO;
import com.manager.dto.ResetPasswordDTO;
import com.manager.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface UserService {

    ResponseEntity<String> login(LoginDTO loginDTO, BindingResult result, HttpServletRequest request);

    ResponseEntity<String> logOut(HttpServletRequest request);

    ResponseEntity<String> forgotPassword(LoginDTO loginDTO, BindingResult result, HttpServletRequest request);

    ResponseEntity<User> updateProfile(ProfileDTO profileDTO, HttpServletRequest request);

    ResponseEntity changePassword(ResetPasswordDTO resetPasswordDTO, BindingResult result, HttpServletRequest request);

    ResponseEntity uploadFile(MultipartFile multipartFile, HttpServletRequest request);

    ResponseEntity<ProfileDTO> profile(HttpServletRequest request);

    ResponseEntity<String> resetPassword(ResetPasswordDTO resetPasswordDTO, String code, int id);
}
