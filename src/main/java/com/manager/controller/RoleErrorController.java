package com.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class RoleErrorController {
    @GetMapping("notLoggedIn")
    public ResponseEntity notLoggedIn(){
        return new ResponseEntity("NOT_LOGGED_IN", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("yourNotAdmin")
    public ResponseEntity notAdmin(){
        return new ResponseEntity("YOUR_NOT_ADMIN", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("yourNotManager")
    public ResponseEntity notManager(){
        return new ResponseEntity("YOUR_NOT_MANAGER", HttpStatus.UNAUTHORIZED);
    }
}
