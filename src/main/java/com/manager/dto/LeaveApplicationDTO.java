package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplicationDTO {
	private int id;
	private String name;
	private String position;
	private long fromDate;
	private long toDate;
	private String reason;
	private String status;

}
