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
		calendar.set(Calendar.DAY_OF_MONTH, 24);
		calendar.set(Calendar.MONDAY, 3 - 1);
		calendar.set(Calendar.YEAR, 1998);

		System.out.println(calendar.getTimeInMillis());

		System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

		Calendar calendar1 = Calendar.getInstance();
		System.out.println(calendar1.getTimeInMillis());
	}

}
