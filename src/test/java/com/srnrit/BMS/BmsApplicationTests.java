package com.srnrit.BMS;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.service.UserService;

@SpringBootTest
class BmsApplicationTests {
	
//	@Autowired
//	UserDao userDAO;

//	@Test
//	void userSaveTest() 
//	{
//		User user = new User();
//		user.setUserName("venu");
//		user.setUserEmail("venu@gmail.com");
//		user.setUserPassword("Venu@!2334");
//		user.setUserGender("Male");
//		user.setUserPhone(8712339224L);
//		user.setTermsAndConditions(true);
//		
//		Optional<User> saveuser = userDAO.saveuser(user);
//		 assertTrue(saveuser.isPresent()); 
//		 System.out.println(saveuser.get());
//		
//		
//	}
	
	@Autowired
	UserService service;
	
//	@Test
//	void saveUserService()
//	{
//		UserRequestDTO user = new UserRequestDTO();
//		user.setUserName("venu");
//		user.setUserEmail("venu@gmail.com");
//		user.setUserPassword("Venu@!2334");
//		user.setUserGender("Male");
//		user.setUserPhone("8712339224");
//		user.setTermsAndConditions(true);
//		UserResponseDTO saveUser = service.saveUser(user);
//		System.out.println(saveUser);
//	}
	
	
	//service layer unit testing
	@Test
	void deleteUserById()
	{
		String msg = service.deleteUserById("Uid_01");
		System.out.println(msg);
	}
	
	

}
