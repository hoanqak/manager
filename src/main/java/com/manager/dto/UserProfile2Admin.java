package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile2Admin {

	private int id;
	private String name;
	private long birthday;
	private String phoneNumber;
	private String email;
	private String position;
	private String status;

}
