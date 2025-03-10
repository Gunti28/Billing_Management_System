package com.srnrit.BMS.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.srnrit.BMS.util.idgenerator.OTPGenerator;

@Component
public class OTPOperation 
{
	 
	
     private Map<String, String> otpStore;

	 public OTPOperation() 
	 {
		this.otpStore=new HashMap<>();
	 }
	 
	 
	 public  void storeOTP(String email,String OTP)
	 {
		this.otpStore.put(email,OTP);
		System.out.println(this.otpStore);
	 }
	 
	 public Optional<String> validateOTP(String email,String OTP)
	 {
		 String storedOTP = this.otpStore.get(email);
		 if(storedOTP!=null)
		 {
			 if(storedOTP.equals(OTP))
			 {
				 this.otpStore.remove(email);
				 return  Optional.of("OTP Validation Successfull");
			 }
			 else
			 {
				 return Optional.empty();
			 }
			 
			 
		 }
		 else 
		 {
			return Optional.empty();
		 }
	 }
	 
	 public String generateOTP()
	 {
		  Optional<StringBuilder> otp = OTPGenerator.generateOTP(4);
		  if(otp.isPresent())
		  {
			 return new String(otp.get()); 
		  }
		  else throw new RuntimeException("OTP  Generation Failed");
	 }
     
     
     
}
