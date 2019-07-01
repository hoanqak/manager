package com.manager.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manager.dto.*;
import com.manager.model.LeaveApplication;
import com.manager.repository.PasswordIssuingCodeRepository;
import com.manager.service.Impl.CheckInOutImpl;
import com.manager.service.Impl.LeaveApplicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manager.repository.CheckInOutRepository;
import com.manager.repository.UserRepository;
import com.manager.service.Impl.UserServiceImpl;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private PasswordIssuingCodeRepository passwordIssuingCodeRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckInOutRepository checkInOutRepository;

    @Autowired
    private LeaveApplicationServiceImpl leaveApplicationService;

    @Autowired
    private CheckInOutImpl checkInOutImpl;

    @Autowired
    private CheckInOutRepository checkInOuRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response,
                                        HttpServletRequest request) {
        return userServiceImpl.login(loginDTO, request);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        return userServiceImpl.logOut(request);
    }

    @GetMapping("profile")
    public ResponseEntity<ProfileDTO> profile(HttpServletRequest request) {
        return userServiceImpl.profile(request);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity upateProfile(ProfileDTO profileDTO) {
        return null;
    }

    //Reset Password
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return userServiceImpl.forgotPassword(loginDTO, request);
    }

    //Send Mail
    @PutMapping("/forgotPassword/{code}/{id}")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, @PathVariable("code") String code, @PathVariable("id") int id) {
        return userServiceImpl.resetPassword(resetPasswordDTO, code, id);
    }

    //Change password
    @PutMapping("changePassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return userServiceImpl.changePassword(resetPasswordDTO);
    }

    @PostMapping("/checkIn")
    public ResponseEntity<String> checkIn(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
        return checkInOutImpl.checkIn(checkInOutDTO, request);
    }

    @PostMapping("checkOut")
    public ResponseEntity CheckOut(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
        return checkInOutImpl.checkOut(checkInOutDTO, request);
    }

    @PostMapping("/requestADayOff")
    public ResponseEntity requestADayOff(@RequestBody RequestADayOffDTO requestADayOffDTO, HttpServletRequest request) {
        return leaveApplicationService.requestADayOff(requestADayOffDTO, request);
    }

    @GetMapping("/listDayOff")
    public ResponseEntity<List<LeaveApplication>> listDayOff(@RequestBody ListADayOffDTO listADayOffDTO, HttpServletRequest request) {
        return leaveApplicationService.listDayOff(listADayOffDTO, request);
    }

    @GetMapping("/test")
    public String test(HttpServletResponse response) {
        response.setHeader("userId", "123");
        return "123";
    }

    @GetMapping("test-1")
    public String get(HttpServletResponse httpServletResponse) {
        return httpServletResponse.getHeader("userId");

    }


}
