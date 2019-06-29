package com.manager.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.ResetPasswordDTO;
import com.manager.model.CheckInOut;
import com.manager.repository.PasswordIssuingCodeRepository;
import com.manager.service.Impl.CheckInOutImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manager.dto.LoginDTO;
import com.manager.model.User;
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
	private CheckInOutImpl checkInOutImpl;

	@Autowired
	private CheckInOutRepository checkInOuRepository;
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response,
			HttpServletRequest request) {
		return userServiceImpl.login(loginDTO, request, response);
	}

	@GetMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
		return userServiceImpl.forgotPassword(loginDTO, request);
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAll() {
		return new ResponseEntity<List<User>>(userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
		User user = null;
		try {
			user = userRepository.findById(id).get();
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (NoSuchElementException n) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("userDTO")
	public ResponseEntity<List<LoginDTO>> get() {
		List<User> list = userRepository.findAll();
		List<LoginDTO> listDTO = new ArrayList<LoginDTO>();
		LoginDTO dto = null;
		for (User user : list) {
			dto = new LoginDTO(user.getEmail(), user.getPassword());
			listDTO.add(dto);
		}

		return new ResponseEntity<List<LoginDTO>>(listDTO, HttpStatus.OK);
	}

	@PutMapping("/reset-password/{code}/{id}")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, @PathVariable("code") String code, @PathVariable("id") int id ){
        return userServiceImpl.resetPassword(resetPasswordDTO, code, id);
    }

    @PutMapping("resetPassword")
	public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
		return userServiceImpl.changePassword(resetPasswordDTO);
	}

    @PostMapping("/checkIn")
	public ResponseEntity<String> checkIn(@RequestBody CheckInOutDTO checkInOutDTO){
		return checkInOutImpl.checkIn(checkInOutDTO);
	}

	@GetMapping("/test")
	public String test(){
		Date date = checkInOuRepository.getDate();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateCheck = simpleDateFormat.format(date);

		Calendar date1 = Calendar.getInstance();
		date1.set(Calendar.DAY_OF_MONTH, 28);
		String dateCheck2 = simpleDateFormat.format(date1.getTime());

		System.out.println(dateCheck + "-----" +dateCheck2);
		System.out.println(dateCheck.equals(dateCheck2));
		return "hihi";
	}

	@PostMapping("checkOut")
	public ResponseEntity CheckOut(@RequestBody CheckInOutDTO checkInOutDTO){
		return checkInOutImpl.checkOut(checkInOutDTO);
	}
	
	
}
