package com.srnrit.BMS.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
	
	@Id
	@Column(name = "UserId")
	private String userId;

	@Column(name = "NAME",length = 30)
	private String userName;
	
	@Column(name="DATEOFREGISTRATION")
	private LocalDateTime dateOfRegistration;

	@Column(name = "ISACTIVE")
	private Boolean active;

	@Column(name = "EMAIL",length = 50)
	private String userEmail;

	@Column(name = "PASSWORD",length = 30)
	private String userPassword;

	@Column(name = "PROFILEIMAGE")
	private String userProfileImage;

	@Column(name = "GENDER",length = 6)
	private String userGender;

	@Column(name = "PHONE",length = 10)
	private Long userPhone;

	@Column(name = "TermsAndConditions")
	private Boolean termsAndConditions;
	
	
	@PrePersist
	public void generateId()
	{
		if(this.userId==null)
		{
			userId=this.generateCustomId();
		}
	}
	
	private String generateCustomId()
	{
		String prefix="UID";
		String suffix="";
		long timeStamp = Instant.now().toEpochMilli();
		int randomPart = ThreadLocalRandom.current().nextInt(100000000, 999999999);
		suffix=timeStamp+""+randomPart;
		
		return prefix+suffix;
		
	}
}
