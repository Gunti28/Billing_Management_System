package com.srnrit.BMS.dao;

import java.util.Optional;
import com.srnrit.BMS.entity.User;

public interface UserDao {
	
	Optional<User> saveuser(User user);
	Optional<String> deleteUser(String userId);
	Optional<User> findByUserId(String userId);
	Optional<User> updateByUserId(User user, String userId);
	Optional<User> findByUserEmail(String userEmail);
	Optional<User> findByUserPhoneNumber(long userPhoneNumber);

}
