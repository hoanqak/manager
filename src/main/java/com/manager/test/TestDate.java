package com.manager.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestDate {

	public static void main(String[] args) throws InterruptedException {
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTimeInMillis());
		calendar.set(Calendar.DAY_OF_MONTH, 5);
		System.out.println(calendar.getTimeInMillis());
	}

}
