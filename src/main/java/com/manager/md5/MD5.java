package com.manager.md5;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class MD5 {

	public String convertToMD5(String input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			messageDigest.update(input.getBytes());
			BigInteger bigInteger = new BigInteger(1, messageDigest.digest());
			return bigInteger.toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new NumberFormatException();
		}
	}

	public static void main(String[] args) {
		System.out.println(new MD5().convertToMD5("12345"));
//        System.out.println(new MD5().convertToMD5("123").length());
	}
}
