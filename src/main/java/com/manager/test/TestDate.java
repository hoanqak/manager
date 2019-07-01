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
		calendar.setTimeInMillis(3495834598345l);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println(simpleDateFormat.format(calendar.getTime()));
	}

}
