package com.srnrit.BMS;

<<<<<<< HEAD
=======
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

>>>>>>> 63bb5fb3aaa44549e4ef47308d365122c0ef17f4
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
<<<<<<< HEAD
import com.srnrit.BMS.service.UserService;

=======
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.repository.UserRepository;

import com.srnrit.BMS.service.UserService;

import static org.junit.jupiter.api.Assertions.*;


>>>>>>> 63bb5fb3aaa44549e4ef47308d365122c0ef17f4
@SpringBootTest
class BmsApplicationTests {
	

	@Autowired
	UserDao userDAO;
	
	@Mock
	UserRepository userRepository ;
	
	
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
//	@Test
//	void deleteUserById()
//	{
//
//		String msg = service.deleteUserById("Uid_01");
//		System.out.println(msg);	
//	}
	
	
	//find user By id in service layer 
	
	@Test
	void findUserById()
	{
		UserResponseDTO userResponse = service.findUserById("Uid_02");
		System.out.println(userResponse);
	}
	
	


		
}
	
	


