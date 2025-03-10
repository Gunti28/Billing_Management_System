package com.srnrit.BMS.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Setter
@Getter
@ToString
public class VerifyOTPRequestDTO implements Serializable  
{
	@NotBlank(message = "Email can't be blank")
	@NotNull(message = "Name can't be null")
	@Pattern(regexp = "^[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message = "Invalid email")
	private String email;
	
	@NotBlank(message = "OTP can't be blank")
	@NotNull(message = "OTP can't be null")
	private String otp;
}
