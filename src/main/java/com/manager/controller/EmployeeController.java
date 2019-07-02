package com.manager.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.manager.dto.*;
import com.manager.model.CheckInOut;
import com.manager.model.LeaveApplication;
import com.manager.service.Impl.CheckInOutServiceImpl;
import com.manager.service.Impl.LeaveApplicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manager.service.Impl.UserServiceImpl;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private LeaveApplicationServiceImpl leaveApplicationService;

    @Autowired
    private CheckInOutServiceImpl checkInOutServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO,
                                        HttpServletRequest request) {
        return userServiceImpl.login(loginDTO, request);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        return userServiceImpl.logOut(request);
    }

    //get profile
    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> profile(HttpServletRequest request) {
        return userServiceImpl.profile(request);
    }

    //update profile
    @PostMapping("/profile")
    public ResponseEntity updateProfile(@RequestBody ProfileDTO profileDTO, HttpServletRequest request) {
        return userServiceImpl.updateProfile(profileDTO, request);
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request){
        return userServiceImpl.uploadFile(multipartFile, request);
    }

    //Reset Password, send mail
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return userServiceImpl.forgotPassword(loginDTO, request);
    }

    //reset password in mail when request forgot password
    @GetMapping("/forgotPassword/{code}/{id}")
    public ResponseEntity<String> resetPasswordInMail(@RequestBody ResetPasswordDTO resetPasswordDTO, @PathVariable("code") String code, @PathVariable("id") int id) {
        return userServiceImpl.resetPassword(resetPasswordDTO, code, id);
    }

    //Change password in profile
    @PutMapping("changePassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {
        return userServiceImpl.changePassword(resetPasswordDTO, request);
    }

    @PostMapping("/checkIn")
    public ResponseEntity<String> checkIn(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
        return checkInOutServiceImpl.checkIn(checkInOutDTO, request);
    }

    @PostMapping("/checkOut")
    public ResponseEntity CheckOut(@RequestBody CheckInOutDTO checkInOutDTO, HttpServletRequest request) {
        return checkInOutServiceImpl.checkOut(checkInOutDTO, request);
    }

    //get list checkInOuts
    @GetMapping("/checkInOuts")
    public ResponseEntity<List<CheckInOut>> checkInOuts(HttpServletRequest request){
        return checkInOutServiceImpl.getListCheckInOut(request);
    }

    @PostMapping("/requestADayOff")
    public ResponseEntity requestADayOff(@RequestBody RequestADayOffDTO requestADayOffDTO, HttpServletRequest request) {
        return leaveApplicationService.requestADayOff(requestADayOffDTO, request);
    }

    @GetMapping("/listDayOff")
    public ResponseEntity<List<LeaveApplication>> listDayOff(@RequestBody ListADayOffDTO listADayOffDTO, HttpServletRequest request) {
        return leaveApplicationService.listDayOff(listADayOffDTO, request);
    }

}
