package com.manager.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestDate {

	public static void main(String[] args) throws InterruptedException {
		/*Calendar calendar = Calendar.getInstance(Locale.forLanguageTag("vi"));
		long time = calendar.getTimeInMillis();
		System.out.println(time);
		Date date = new Date();
		date.setTime(time);

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(time);


		System.out.println(calendar1.getTime());
*/
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 12);

		System.out.println(calendar.getTimeInMillis());
	}

}
