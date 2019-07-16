package com.manager.test;

import com.manager.repository.CheckInOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TestDate {

	public static void main(String[] args) throws InterruptedException {
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTimeInMillis());
	}

}
