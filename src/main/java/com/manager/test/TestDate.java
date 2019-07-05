package com.manager.test;

import com.manager.model.CheckInOut;
import com.manager.repository.CheckInOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TestDate {

	@Autowired
	static CheckInOutRepository checkInOutRepository;
	public static void main(String[] args) throws InterruptedException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 31);
		calendar.set(Calendar.SECOND, 0);
		System.out.println(calendar.getTimeInMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		System.out.println(calendar.getTimeInMillis());
	}

}
