package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

	private int id;
	private long birthday;
	private long startedDate;
	private String position;
	private String phoneNumber;
	private String email;
	private String picture;
	private String name;
	private String status;
}
