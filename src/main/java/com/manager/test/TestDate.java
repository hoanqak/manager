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
//		System.out.println(new MD5().convertToMD5("12345678"));
		Date date = new Date(121212021020200l);
		System.out.println(date);
		System.out.println(date2String(date));

	}

	public static String date2String(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1);
	}


}
