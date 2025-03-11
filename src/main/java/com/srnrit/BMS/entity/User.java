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

	@Column(name = "NAME",length = 30)
	private String userName;
	
	@Column(name="DATEOFREGISTRATION")
	private LocalDateTime dateOfRegistration;

	@Column(name = "ISACTIVE")
	private Boolean active;

	@Column(name = "EMAIL",length = 30)
	private String userEmail;

	@Column(name = "PASSWORD",length = 30)
	private String userPassword;

	@Column(name = "PROFILEIMAGE",length = 30)
	private String userProfileImage;

	@Column(name = "GENDER",length = 6)
	private String userGender;

	@Column(name = "PHONE",length = 10)
	private Long userPhone;

	@Column(name = "TermsAndConditions")
	private Boolean termsAndConditions;
}
