package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

	private String avatar;
	private String fullName;
	private long dateOfBirth;
	private String position;
	private String phoneNumber;
	private String email;
	private long startDate;


}
