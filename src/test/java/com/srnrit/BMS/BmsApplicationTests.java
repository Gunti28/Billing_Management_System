package com.srnrit.BMS;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.repository.UserRepository;

@SpringBootTest
class BmsApplicationTests {
	
	@Autowired
	UserDao userDAO;
	
	@Mock
	UserRepository userRepository ;
	
	

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
	
	


