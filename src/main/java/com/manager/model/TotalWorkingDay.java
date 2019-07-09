package com.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalWorkingDay {
	private int userId;
	private String name;
	private int position;
	private double total;
//	Key là dayCheckIn, giá trị dạng Date.
//	Integer là giá trị totalTime.
	private Map<String, Integer> days;

}
