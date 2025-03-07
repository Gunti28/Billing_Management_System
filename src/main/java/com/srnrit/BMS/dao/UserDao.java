package com.srnrit.BMS.dao;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.entity.User;

public interface UserDao {
	
	Optional<User> saveuser(User user);
	Optional<String> deleteUserById(String userId);
	Optional<User> findByUserId(String userId);
	Optional<User> updateByUserId(User user, String userId);
	Optional<User> findByUserEmail(String userEmail);
	Optional<User> findByUserPhoneNumber(long userPhoneNumber);
	Optional<User> loginByEmailAndPassword(String userEmail,String userPassword);
	Optional<User> editImage(MultipartFile file,String userId);
	
}
