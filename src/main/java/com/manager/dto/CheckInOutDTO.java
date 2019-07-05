package com.manager.dto;

import com.manager.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dozer.Mapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckInOutDTO {
	private int id;
//	private int id_user;
//	private String name;
//	private int position;
	private long startTime;
	private long endTime;
	private int totalTime;
	private long timeCheck;
	private long dayCheckIn;
	private User user;

}