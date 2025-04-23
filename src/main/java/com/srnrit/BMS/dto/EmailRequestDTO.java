package com.srnrit.BMS.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class EmailRequestDTO implements Serializable
{
	@NotBlank(message = "Email can't be blank or null !")
	@Pattern(regexp = "^(?!\\s*$)[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message = "Invalid email")
	private String email;
   
}
