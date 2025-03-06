package com.srnrit.BMS;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.entity.User;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BmsApplicationTests {
	
	@Autowired
	UserDao userDAO;

	@Test
	void userSaveTest() 
	{
		User user = new User();
		user.setUserName("venu");
		user.setUserEmail("venu@gmail.com");
		user.setUserPassword("Venu@!2334");
		user.setUserGender("Male");
		user.setUserPhone(8712339224L);
		user.setTermsAndConditions(true);
		
		Optional<User> saveuser = userDAO.saveuser(user);
		 assertTrue(saveuser.isPresent()); 
		 System.out.println(saveuser.get());
		
		
	}

}
