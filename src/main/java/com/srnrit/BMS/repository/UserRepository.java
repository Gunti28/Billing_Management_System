package com.srnrit.BMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.srnrit.BMS.entity.User;
public interface UserRepository extends JpaRepository<User, String>{
	
	User findByUserEmail(String userEmail);
	User findByUserPassword(String userPassword);
	User findByUserPhone(Long userphonenumber);
	User findByUserEmailAndUserPassword(String userEmail,String userPassword);
	Optional<User> findByUserId(String userId);
	
}
