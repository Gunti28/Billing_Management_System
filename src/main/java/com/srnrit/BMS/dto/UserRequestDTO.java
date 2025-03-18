package com.srnrit.BMS.dto;

import java.io.Serializable;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
public class UserRequestDTO implements Serializable{
	
	@NotBlank(message = "Name can't be blank or null !")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	@Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$",message = "Name must contain only alphabets")
	private String userName;
	
	@NotBlank(message = "Email can't be blank or null !")
	@Pattern(regexp = "^(?!\\s*$)[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message = "Invalid email")
	private String userEmail;
	
	
	@NotBlank(message = "Password can't be blank or null !")
	@Size(min=6,message = "Password must be at least 6 characters")
	private String userPassword;
	
	
	@NotBlank(message = "Gender can't be blank or null !")
	@Pattern(regexp = "^[A-Za-z ]+$",message = "Gender must contain only alphabets")
	@Pattern(regexp = "Male|Female|Other", message = "Gender must be 'Male', 'Female', or 'Other'")
	private String userGender;
	
	@NotBlank(message = "Mobile number can't be blank or null !")
	@Positive(message = "Mobile number must be positive")
	@Pattern(regexp = "^[1-9][0-9]{9}$",message = "Mobile number must be 10 digits and must not be started with 0")
	private String userPhone;
	
	@AssertTrue(message = "You must accept the terms and conditions.")
	private Boolean termsAndConditions;
	

}
