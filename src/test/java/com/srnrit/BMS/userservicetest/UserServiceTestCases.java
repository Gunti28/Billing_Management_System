package com.srnrit.BMS.userservicetest;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dto.EmailRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.service.UserService;
import com.srnrit.BMS.util.Message;

@SpringBootTest
public class UserServiceTestCases {

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
	
//	@Test
//	void findUserById()
//	{
//		UserResponseDTO userResponse = service.findUserById("Uid_02");
//		System.out.println(userResponse);
//	}
	
	
//	@Test
//	void loginEmailAndPassword()
//	{
//		UserResponseDTO userResponseDTO = service.loginUserByEmailAndPassword("venu123@gmail.com", "venu123");
//		System.out.println(userResponseDTO);
//	}
//	
//	@Test
//	void getAllUser()
//	{
//		List<UserResponseDTO> allUsers = service.getAllUsers();
//		System.out.println(allUsers);
//	}

	
	
	@Test
	void verifyEmail()
	{
		EmailRequestDTO emailRequestDTO=new EmailRequestDTO();
		emailRequestDTO.setEmail("sujitmaharana1111@gmail.com");
		Message verifyUserByEmail = this.service.verifyUserByEmail(emailRequestDTO);
		System.out.println(verifyUserByEmail);	
	}
}
