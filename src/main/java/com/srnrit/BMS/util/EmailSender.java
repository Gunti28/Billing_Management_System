package com.srnrit.BMS.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender 
{
	
	private static JavaMailSender javaMailSender;
	
	public EmailSender(JavaMailSender javaMailSender) 
	{
		EmailSender.javaMailSender=javaMailSender;
	}
	 
	
	public static void sendOtpEmail(String to, String otp) 
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Your Otp Code");
		message.setText("Your Otp Code is :" + otp);
		javaMailSender.send(message);
	}

	

}
