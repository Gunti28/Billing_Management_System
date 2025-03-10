package com.srnrit.BMS.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender 
{
	
	 
	 private static JavaMailSender javaMailSender;
	 
	 
	
	
	
	public EmailSender(JavaMailSender javaMailSender) {
		super();
		EmailSender.javaMailSender = javaMailSender;
	}





	public static boolean sendOTPToEmail(String to, String otp) 
	{
		
		boolean flag=false;
		try 
		{
			
			SimpleMailMessage message = new SimpleMailMessage();
			
			message.setTo(to);
			message.setSubject("Your Otp Code");
			message.setText("Your Otp Code is :" + otp);
			javaMailSender.send(message);
			flag=true;
		} 
		catch (Exception e)
		{
		      throw new RuntimeException(e.getMessage());
		}
		
		return flag;
	}

	

}
