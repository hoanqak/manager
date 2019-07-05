package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalWorkingDayDTO {
	private int userId;
	private String name;
	private int position;
	private double total;
	//	Long là giá trị chuyển sang từ dạng Date của dayCheckIn.
//	Integer là giá trị totalTime.
	private Map<Long, Integer> days;

}
