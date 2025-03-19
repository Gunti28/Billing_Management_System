package com.srnrit.BMS.dto;

import java.io.Serializable;

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
public class UserResponseDTO implements Serializable
{
	
	private String userId;
	private String userName;
	private String userEmail;
	private String userPassword;
	private Boolean active;
	private String userProfileImage;
	private String userGender;
	private Long userPhone;
	private Boolean termsAndConditions;

}
