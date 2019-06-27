package com.manager.service;

import com.manager.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface UserService {

    public ResponseEntity login(String email, String password);
    public ResponseEntity<Boolean> forgotPassword(String email);
    public ResponseEntity<User> getUserById(int id);
    public ResponseEntity<User> editProfile(int id);
    public ResponseEntity checkIn();
    public ResponseEntity checkOut();
    public ResponseEntity requestADayOff(Date fromDate, Date toDate, String reason);

}
