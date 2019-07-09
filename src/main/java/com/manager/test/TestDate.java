package com.manager.test;

import com.manager.md5.MD5;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestDate {

	public static void main(String[] args) throws InterruptedException {
//		System.out.println(new MD5().convertToMD5("12345678"));
		Date date = new Date(121212021020200l);
		System.out.println(date);
		System.out.println(date2String(date));

	}
	public static String date2String(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1);
	}


}
