package com.srnrit.BMS.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserRequestDTO implements Serializable{
	
  @NotBlank(message="Name Can't Be Blank")
  @NotNull(message="Name Can't be Null")
  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
  @Pattern(regexp = "^[A-Za-z ]+$",message = "Name must Combine Uppercase and LowerCase letters")
  private String userName;

  @NotBlank(message="E-mail Can't Be Blank")
  @NotNull(message="E-mail Can't be Null")
  @Pattern(regexp = "^[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message="Invalid email")
  private String userEmail;
  
  @Positive(message = "Mobile no must be positive")
  @Pattern(regexp = "^[1-9][0-9]{9}$",message = "Mobile number must be 10 digits and must not be started with 0")
  private String userPhone;

}
