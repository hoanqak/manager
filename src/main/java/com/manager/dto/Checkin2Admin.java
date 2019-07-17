package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Checkin2Admin {

	private int id;
	private int userId;
	private int totalTime;
	private String name;
	private String position;
	private long startTime;
	private long endTime;

}
