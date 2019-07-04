package com.manager.dto;

import com.manager.model.CheckInOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckInOutDTO {
	private int id;
	private int id_user;
	private String name;
	private int position;
	private long checkin;
	private long checkout;
	private int total;
	private long timeCheck;

	public CheckInOutDTO(CheckInOut checkInOut) {
		this.id = checkInOut.getId();
		this.id_user = checkInOut.getUser().getId();
		this.name = checkInOut.getUser().getName();
		this.checkin = checkInOut.getStartTime().getTime();
		this.checkout = checkInOut.getEndTime().getTime();
		this.total = (int) checkInOut.getTotalTime();
		this.position = checkInOut.getUser().getPosition();

	}
}