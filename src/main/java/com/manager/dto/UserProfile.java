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
	private String picture;
	private String name;
	private long birthday;
	private int position;
	private String phoneNumber;
	private String email;
	private long startedDate;
}
