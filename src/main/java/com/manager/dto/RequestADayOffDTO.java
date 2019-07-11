package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class RequestADayOffDTO {
	private long fromDate;
	private long toDate;
	private String reason;
	private int totalDayOff;
	private int remainDayOff;
	private String status;

}
