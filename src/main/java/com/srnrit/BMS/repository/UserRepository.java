package com.srnrit.BMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srnrit.BMS.entity.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	User findByUserEmail(String userEmail);
	User findByUserPhone(Long userphonenumber);
	User findByUserEmailAndUserPassword(String userEmail,String userPassword);
}
