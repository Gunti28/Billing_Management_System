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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserRequestDTO implements Serializable{
	
  @NotBlank(message="Name Can't Be Blank or null !")
  @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must Combine Uppercase and LowerCase letters")
  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
  private String userName;

  @NotBlank(message="E-mail can't be blank or null !")
  @Pattern(regexp = "^(?!\\s*$)[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message="Invalid email")
  private String userEmail;
  
  @NotBlank(message="Phone Number can't Be blank or null !")
  @Pattern(regexp = "^[1-9][0-9]{9}$",message = "Phone Number must be 10 digits and not started with 0")
  private String userPhone;

}
