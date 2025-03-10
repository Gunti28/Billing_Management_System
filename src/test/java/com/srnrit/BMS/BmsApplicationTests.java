package com.srnrit.BMS;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dao.impl.UserDaoImpl;
import com.srnrit.BMS.dto.EmailRequestDTO;
import com.srnrit.BMS.repository.UserRepository;
import com.srnrit.BMS.service.impl.UserServiceImpl;
import com.srnrit.BMS.util.FileStorageProperties;
import com.srnrit.BMS.util.Message;

@SpringBootTest
class BmsApplicationTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private FileStorageProperties fileStorageProperties;

	@InjectMocks
	private UserDaoImpl userDao;

	@InjectMocks
	private UserServiceImpl userService;

	

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

	// service layer unit testing
//	@Test
//	void deleteUserById()
//	{
//
//		String msg = service.deleteUserById("Uid_01");
//		System.out.println(msg);	
//	}

	// find user By id in service layer

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

//	@Test
//	void getAllUser()
//	{
//		List<UserResponseDTO> allUsers = service.getAllUsers();
//		System.out.println(allUsers);
//	}

	@Test
	void verifyEmail() {
		EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
		emailRequestDTO.setEmail("sujitmaharana1111@gmail.com");
		Message verifyUserByEmail = this.userService.verifyUserByEmail(emailRequestDTO);
		System.out.println(verifyUserByEmail);

	}

}
