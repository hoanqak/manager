package com.manager.controller;

import com.manager.data.Notifications;
import com.manager.dto.LoginDTO;
import com.manager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
public class RoleErrorController {
	@GetMapping("notLoggedIn")
	public ResponseEntity notLoggedIn() {
		return new ResponseEntity(Notifications.NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("yourNotAdmin")
	public ResponseEntity notAdmin() {
		return new ResponseEntity(Notifications.YOUR_NOT_ADMIN, HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("yourNotManager")
	public ResponseEntity notManager() {
		return new ResponseEntity(Notifications.YOUR_NOT_MANAGER, HttpStatus.UNAUTHORIZED);
	}

//	@Autowired
//	AuthenticationManager authentication;
//
//	@PostMapping("/signin")
//	public ResponseEntity login(@RequestBody LoginDTO loginDTO){
//		System.out.println("in");
//		Authentication authenticationManager = authentication.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
//		SecurityContextHolder.getContext().setAuthentication(authenticationManager);
//		User user = (User) SecurityContextHolder.getContext().getAuthentication();
//
//		System.out.println(user.getEmail());
//		return new ResponseEntity(user, HttpStatus.OK);
//
//	}
}
