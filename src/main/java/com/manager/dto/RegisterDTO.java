package com.manager.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
	
	private String name;
	private Date dateOfBirth;
	private String position;
	private String phoneNumber;
	private String email;

}
