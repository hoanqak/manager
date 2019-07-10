package com.manager.test;

import com.manager.repository.CheckInOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TestDate {

	@Autowired
	static CheckInOutRepository checkInOutRepository;

	public static void main(String[] args) throws InterruptedException {
		Long.parseLong("4543gfg");
	}

	public static String date2String(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1);
	}


}
