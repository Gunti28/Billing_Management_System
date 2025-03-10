package com.srnrit.BMS.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@SuppressWarnings("serial")
public class EmailRequestDTO implements Serializable
{
	@NotBlank(message = "Email can't be blank")
	@NotNull(message = "Name can't be null")
	@Pattern(regexp = "^[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message = "Invalid email")
	private String email;
   
}
