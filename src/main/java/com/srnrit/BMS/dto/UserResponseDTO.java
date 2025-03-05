package com.srnrit.BMS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDTO {
	
	private String userId;
	private String userName;
	private String userEmail;
	private String userPassword;
	private Boolean active;
	private String profileImage;
	private String userGender;
	private Long userPhone;
	private Boolean termsAndConditions;

}
