package com.manager.test;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {
	public static void main(String args[]) throws AddressException, MessagingException {
		sendText();
		/*
		 * sendHTML(); sendFile();
		 */
		System.out.println("done");
	}

	public static void sendText() throws AddressException, MessagingException {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage mailMessage;

		// Step1: setup Mail Server
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		
		  mailServerProperties.put("mail.smtp.starttls.enable", "true");
		 
		// Step2: get Mail Session
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		mailMessage = new MimeMessage(getMailSession);

		// thay abc@gmail.com bằng địa chỉ người nhận
		mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("tranhuyhoang240398@gmail.com")); 

		// Bạn có thể chọn CC, BCC
		// generateMailMessage.addRecipient(Message.RecipientType.CC, new
		// InternetAddress("cc@gmail.com")); //Địa chỉ cc gmail

		mailMessage.setSubject("Demo send gmail from Java");
		mailMessage.setText("Demo send text by gmail from Java");

		// Step3: Send mail
		Transport transport = getMailSession.getTransport("smtp");

		// Thay your_gmail thành gmail của bạn, thay your_password thành mật khẩu gmail của bạn
		transport.connect("smtp.gmail.com", "scofjeld@gmail.com", "saranghae123");
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		transport.close();
	}

	public static void sendHTML() throws AddressException, MessagingException {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage mailMessage;

		// Step1: setup Mail Server
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		// Step2: get Mail Session
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		mailMessage = new MimeMessage(getMailSession);

		// thay abc@gmail.com bằng địa chỉ người nhận
		mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("abc@gmail.com")); 

		// Bạn có thể chọn CC, BCC
		// generateMailMessage.addRecipient(Message.RecipientType.CC, new
		// InternetAddress("cc@gmail.com")); //Địa chỉ cc gmail

		mailMessage.setSubject("Demo send gmail from Java");
		String emailBody = "<p style='color: red'>Demo send HTML from Java<p>";
		mailMessage.setContent(emailBody, "text/html");

		// Step3: Send mail
		Transport transport = getMailSession.getTransport("smtp");

		// Thay your_gmail thành gmail của bạn, thay your_password
		// thành mật khẩu gmail của bạn
		transport.connect("smtp.gmail.com", "your_gmail", "your_password");
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		transport.close();
	}

	public static void sendFile() throws AddressException, MessagingException {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage mailMessage;

		// Step1: setup Mail Server
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		// Step2: get Mail Session
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		mailMessage = new MimeMessage(getMailSession);

		
		// thay abc@gmail.com bằng địa chỉ người nhận
		mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("abc@gmail.com"));
		mailMessage.setSubject("Demo send gmail from Java");
		
		// Bạn có thể chọn CC, BCC
		// generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("cc@gmail.com")); //Địa chỉ cc gmail

		// Tạo phần gửi message
		BodyPart messagePart = new MimeBodyPart();
		messagePart.setText("Demo send file by Gmail from Java");

		// Tạo phần gửi file
		BodyPart filePart = new MimeBodyPart();
		
		// thay C:\\a.txt thành file mà bạn muốn gửi
		File file = new File("C:\\a.txt");
		DataSource source = new FileDataSource(file);
		filePart.setDataHandler(new DataHandler(source));
		filePart.setFileName(file.getName());

		// Gộp message và file vào để gửi đi
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messagePart);
		multipart.addBodyPart(filePart);
		mailMessage.setContent(multipart);

		// Step3: Send mail
		Transport transport = getMailSession.getTransport("smtp");

		// Thay your_gmail thành gmail của bạn, thay your_password thành mật khẩu gmail của bạn
		transport.connect("smtp.gmail.com", "your_gmail", "your_password");
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		transport.close();
	}
}
