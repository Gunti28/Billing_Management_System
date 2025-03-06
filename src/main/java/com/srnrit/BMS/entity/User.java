package com.srnrit.BMS.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import com.srnrit.BMS.util.idgenerator.UserIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_TABLE")
public class User {
	@SuppressWarnings("deprecation")
	@Id
	@GenericGenerator(name = "user_id_generator", type = UserIdGenerator.class)
	@GeneratedValue(generator = "user_id_generator")
	@Column(name = "UserId",length = 20)
	private String userId;

	@Column(name = "UseName")
	private String userName;
	
	@Column(name="DateOfRegistration")
	private LocalDateTime dateOfRegistration;

	@Column(name = "IsActive")
	private Boolean active;

	@Column(name = "UserEmail")
	private String userEmail;

	@Column(name = "UserPassword")
	private String userPassword;

	@Column(name = "UserProfileImage")
	private String userProfileImage;

	@Column(name = "Usergender")
	private String userGender;

	@Column(name = "UserPhone")
	private Long userPhone;

	@Column(name = "TermsAndConditions")
	private Boolean termsAndConditions;
}
