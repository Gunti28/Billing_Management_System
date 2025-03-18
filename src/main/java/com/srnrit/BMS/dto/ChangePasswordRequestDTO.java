package com.srnrit.BMS.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangePasswordRequestDTO implements Serializable 
{
	@NotBlank(message = "Email can't be blank or null !")
	@Pattern(regexp = "^[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message = "Invalid email")
	private String  email;
	
	@NotBlank(message = "Password can't be blank or null !")
	@Size(min=6,message = "Password must be at least 6 characters")
	private String newPassword;
	
	@NotBlank(message = "Password can't be blank or null !")
	@Size(min=6,message = "Password must be at least 6 characters")
	private String confirmPassword;
	
}
