package com.srnrit.BMS.controllertest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.controller.UserController;
import com.srnrit.BMS.dto.ChangePasswordRequestDTO;
import com.srnrit.BMS.dto.EmailRequestDTO;
import com.srnrit.BMS.dto.LoginRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.dto.VerifyOTPRequestDTO;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.globalexcepiton.GlobalExceptionHandler;
import com.srnrit.BMS.service.UserService;
import com.srnrit.BMS.util.Message;

 
  @WebMvcTest(UserController.class)
  @ExtendWith(MockitoExtension.class)
  @Import(GlobalExceptionHandler.class)
  
  public class UserControllerTest {


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("removal")
	@MockBean
	private UserService userService;

	private UserRequestDTO userRequestDTO;
	private UserResponseDTO userResponseDTO;

	private User user = new User();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
//		mockMvc = MockMvcBuilders.standaloneSetup(userController)
//				.setControllerAdvice(new GlobalExceptionHandler())
//				.build();
//				

		userRequestDTO = new UserRequestDTO();
		userRequestDTO.setTermsAndConditions(true);
		userRequestDTO.setUserEmail("sujit12345@gmail.com");
		userRequestDTO.setUserGender("Male");
		userRequestDTO.setUserName("Sujit Maharana");
		userRequestDTO.setUserPassword("sujit345");
		userRequestDTO.setUserPhone("6370270394");
		userResponseDTO = new UserResponseDTO();
		BeanUtils.copyProperties(userRequestDTO, userResponseDTO);
		userResponseDTO.setUserId("Uid_01");
		userResponseDTO.setUserProfileImage("default.png");
		userResponseDTO.setActive(true);
		userResponseDTO.setUserPhone(6370270394l);
		BeanUtils.copyProperties(userRequestDTO, user);

	}

	// 1. successful creation of user
	@Test
	void testCreateUser_Success() throws Exception {
		System.out.println("UserControllerTest.testCreateUser_Success()");
		// information sending to the mock userService
		when(userService.saveUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

		// convert DTO to JSON
		String jsonUserRequest = objectMapper.writeValueAsString(userRequestDTO);

		MvcResult result = mockMvc
				.perform(
						 post("/user/create")
						 .contentType(MediaType.APPLICATION_JSON)
						 .content(jsonUserRequest)
						 )
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userName").value("Sujit Maharana"))
				.andExpect(jsonPath("$.userEmail").value("sujit12345@gmail.com"))
				.andExpect(jsonPath("$.userGender").value("Male"))
				.andExpect(jsonPath("$.termsAndConditions").value(true))
				.andExpect(jsonPath("$.userPassword").value("sujit345"))
				.andExpect(jsonPath("$.userPhone").value(6370270394L))
				.andExpect(jsonPath("$.userProfileImage").value("default.png"))
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.userId").value("Uid_01"))
				.andExpect(header().string("Location", is("/user/Uid_01")))
				.andReturn();

		System.err.println("Status : " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());

	}

	// 2. Test invalid user information
	@Test
	void testCreateUser_BadRequest() throws Exception {
		System.out.println("UserControllerTest.testCreateUser_BadRequest()");
		UserRequestDTO invalidRequest = new UserRequestDTO();
		invalidRequest.setTermsAndConditions(false);
		invalidRequest.setUserEmail("sujitmaharana1111@gmal.com");
		invalidRequest.setUserGender("  ");
		invalidRequest.setUserName("   ");
		invalidRequest.setUserPassword("   ");
		invalidRequest.setUserPhone("6370270394");

		String requestBody = objectMapper.writeValueAsString(invalidRequest);
		MvcResult result = mockMvc
				.perform(
						post("/user/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						)
				.andExpect(status().isBadRequest())
				.andReturn();

		System.err.println("Status code : " + result.getResponse().getStatus());
		// getting json data form MockResponse means from "MvcResult result"
		String jsonValidationErrors = result.getResponse().getContentAsString();

		@SuppressWarnings("unchecked")
		Map<String, String> jsonMap = objectMapper.readValue(jsonValidationErrors, Map.class);

		// Print Key-Value format
		System.err.println("Validation Errors:");
		System.err.println("=============================");
		for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
			System.err.println(entry.getKey() + " → " + entry.getValue());
		}

	}

	// 3. Test Unsupported Media Type
	@Test
	void testCreateUser_UnsupportedMediaType() throws Exception {
		System.out.println("UserControllerTest.testCreateUser_UnsupportedMediaType()");
		MvcResult result = mockMvc
				.perform(
						post("/user/create")
						.contentType(MediaType.TEXT_PLAIN) // Unsupported media
						.content(objectMapper.writeValueAsString(userRequestDTO))
						)
				.andExpect(status().isUnsupportedMediaType())
				.andReturn();
		System.err.println("Status: " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());
	}

	// 4.Test with invalid userId in get user api
	@Test
	void testGetUser_withInvalidId() throws Exception {
		System.out.println("UserControllerTest.testGetUser_withInvalidId()");
		String userId = "null";
		when(userService.findUserById(userId))
				.thenThrow(new UserNotFoundException("User not found with id : " + userId));

		MvcResult result = mockMvc
				.perform(
						get("/user/get/" + userId)
						.contentType(MediaType.APPLICATION_JSON)
						)
				.andExpect(status().isNotFound())
				.andReturn();

		System.err.println("status : " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());

	}

	// 5.Test with valid userId in get user api
	@Test
	void testGetUser_withValidId() throws Exception {
		System.out.println("UserControllerTest.testGetUser_withValidId()");
		String userId = "Uid_01";
		when(userService.findUserById(anyString())).thenReturn(userResponseDTO);

		MvcResult result = mockMvc
				.perform(
						get("/user/get/" + userId)
						.contentType(MediaType.APPLICATION_JSON)
						)
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.userEmail").value("sujit12345@gmail.com"))
				.andExpect(jsonPath("$.userId").value("Uid_01"))
				.andReturn();

		System.out.println("status : " + result.getResponse().getStatus());

		String stringJsonUserResponseDTO = result.getResponse().getContentAsString();

		@SuppressWarnings("unchecked")
		Map<String, Object> responseMap = objectMapper.readValue(stringJsonUserResponseDTO, Map.class);

		System.err.println("UserResponseData");
		System.err.println("========================");
		// printing the userResponseDTO in json
		for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
			System.err.println(entry.getKey() + " → " + entry.getValue());
		}
	}

	// 6.Test with blank userId in get user api
	@Test
	void testGetUser_withBlankId() throws Exception {
		System.out.println("UserControllerTest.testGetUser_withBlankId()");
		MvcResult result = mockMvc
				.perform(
						get("/user/get/" + "  ")
						.contentType(MediaType.APPLICATION_JSON)
						)
				.andExpect(status().isBadRequest()) // or isNotFound() if API throws 404
				.andReturn();

		System.err.println("status : " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());
	}

	// 7.use deleteUserById rest api (positive scenario)
	@Test
	void deleteUserById_withRealUserId() throws Exception {
		System.out.println("UserControllerTest.deleteUserById_withRealUserId()");
		String userId = "Uid_01";
		when(userService.deleteUserById(anyString())).thenReturn("User deleted successfull");

		MvcResult result = mockMvc
				.perform(
						delete("/user/" + userId)
						.contentType(MediaType.APPLICATION_JSON)
						)
				.andExpect(status().isOk())
				.andReturn();
		System.err.println("Status code : " + result.getResponse().getStatus());
		System.err.println("Response : " + result.getResponse().getContentAsString());
	}

	// 8.use deleteUserById rest api (negative scenario)
	@Test
	void deleteUserById_withNotExistUserId() throws Exception {
		System.out.println("UserControllerTest.deleteUserById_withNotExistUserId()");
		String userId = "Uid_02";

		doThrow(new UserNotFoundException("User not exist with id : " + userId)).when(userService)
				.deleteUserById(anyString());

		MvcResult result = mockMvc
				           .perform(
				        		   delete("/user/" + userId)
				        		   .contentType(MediaType.APPLICATION_JSON)
				        		   )
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("User not exist with id : " + userId))
				.andReturn();

		System.err.println("Status code : " + result.getResponse().getStatus());
		System.err.println("Response : " + result.getResponse().getContentAsString());

	}

	

	// 9.use deleteUserById rest api (negative scenario where UserId="")
	@Test
	void deleteUserById_withBlankUserId() throws Exception {
		System.out.println("UserControllerTest.deleteUserById_withBlankUserId()");
		String userId = " ";
		MvcResult result = mockMvc
							.perform(
									delete("/user/" + userId)
									.contentType(MediaType.APPLICATION_JSON)
									)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("User ID can't be blank or null!"))
				.andReturn();

		System.err.println("Status code : " + result.getResponse().getStatus());
		System.err.println("Response : " + result.getResponse().getContentAsString());

	}

	// (positive scenario)
	// 10.testing userLoginEmailAndPassword with valid email and password
	@Test
	void testingUserLoginEmailAndPasswordWithValidData() throws Exception {
		System.out.println("UserControllerTest.testingUserLoginEmailAndPasswordWithValidData()");
		String email = "sujitmaharana1111@gmail.com";
		String password = "sujit@123";

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setEmail(email);
		loginRequestDTO.setPassword(password);

		userResponseDTO.setUserEmail(email);
		userResponseDTO.setUserPassword(password);

		String jsonLoginRequestDTO = this.objectMapper.writeValueAsString(loginRequestDTO);

		when(userService.loginUserByEmailAndPassword(anyString(), anyString())).thenReturn(userResponseDTO);

		MvcResult result = mockMvc
							.perform(
									post("/user/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(jsonLoginRequestDTO)
									)
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.userName").value("Sujit Maharana"))
				.andExpect(jsonPath("$.userEmail").value(email))
				.andExpect(jsonPath("$.userGender").value("Male"))
				.andExpect(jsonPath("$.termsAndConditions").value(true))
				.andExpect(jsonPath("$.userPassword").value(password))
				.andExpect(jsonPath("$.userPhone").value(6370270394L))
				.andExpect(jsonPath("$.userProfileImage").value("default.png"))
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.userId").value("Uid_01"))
				.andReturn();

		System.err.println("Status : " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());
	}

	// 11.testing userLoginEmailAndPassword with blank email and password
	@Test
	void testingUserLoginEmailAndPasswordWithBlankData() throws Exception {
		System.out.println("UserControllerTest.testingUserLoginEmailAndPasswordWithBlankData()");
		String email = " ";
		String password = "";

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setEmail(email);
		loginRequestDTO.setPassword(password);
		String jsonInvalidLoginRequestDTO = this.objectMapper.writeValueAsString(loginRequestDTO);

		MvcResult result = mockMvc
								.perform(
										post("/user/login")
										.contentType(MediaType.APPLICATION_JSON)
										.content(jsonInvalidLoginRequestDTO)
										)
				.andExpect(status().isBadRequest())
				.andReturn();

		System.err.println("Status code : " + result.getResponse().getStatus());
		// getting json data form MockResponse means from "MvcResult result"
		String jsonValidationErrors = result.getResponse().getContentAsString();

		@SuppressWarnings("unchecked")
		Map<String, String> jsonMap = objectMapper.readValue(jsonValidationErrors, Map.class);

		// Print Key-Value format
		System.err.println("Validation Errors:");
		System.err.println("=============================");
		for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
			System.err.println(entry.getKey() + " → " + entry.getValue());
		}
	}

	// 12.testing userLoginEmailAndPassword with null data
	@Test
	void testingUserLoginEmailAndPasswordWithNullData() throws Exception {
		System.out.println("UserControllerTest.testingUserLoginEmailAndPasswordWithNullData()");
		String email = null;
		String password = null;

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setEmail(email);
		loginRequestDTO.setPassword(password);
		String jsonInvalidLoginRequestDTO = this.objectMapper.writeValueAsString(loginRequestDTO);

		MvcResult result = mockMvc
							.perform(
									post("/user/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(jsonInvalidLoginRequestDTO)
									)
				.andExpect(status().isBadRequest())
				.andReturn();

		System.err.println("Status code : " + result.getResponse().getStatus());
		// getting json data form MockResponse means from "MvcResult result"
		String jsonValidationErrors = result.getResponse().getContentAsString();

		@SuppressWarnings("unchecked")
		Map<String, String> jsonMap = objectMapper.readValue(jsonValidationErrors, Map.class);

		// Print Key-Value format
		System.err.println("Validation Errors:");
		System.err.println("=============================");
		for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
			System.err.println(entry.getKey() + " → " + entry.getValue());
		}
	}

	// 13.testing userLoginEmailAndPassword with inValid email and password
	@Test
	void testingUserLoginEmailAndPasswordWithInValidData() throws Exception {
		System.out.println("UserControllerTest.testingUserLoginEmailAndPasswordWithInValidData()");
		String email = "sujitmaharana111@gmail.co";
		String password = "sujit";

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setEmail(email);
		loginRequestDTO.setPassword(password);
		String jsonInvalidLoginRequestDTO = this.objectMapper.writeValueAsString(loginRequestDTO);

		MvcResult result = mockMvc
							.perform(
									post("/user/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(jsonInvalidLoginRequestDTO)
									)
				.andExpect(status().isBadRequest())
				.andReturn();

		System.err.println("Status code : " + result.getResponse().getStatus());
		// getting json data form MockResponse means from "MvcResult result"
		String jsonValidationErrors = result.getResponse().getContentAsString();

		@SuppressWarnings("unchecked")
		Map<String, String> jsonMap = objectMapper.readValue(jsonValidationErrors, Map.class);

		// Print Key-Value format
		System.err.println("Validation Errors:");
		System.err.println("=============================");
		for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
			System.err.println(entry.getKey() + " → " + entry.getValue());
		}
	}

	// 14. testing userLoginEmailAndPassword with correct email and invalid password
	// throwing UserNotFoundException
	@Test
	void testingUserLoginEmailAndPasswordWithValidEmailInvalidPassword() throws Exception {
		System.out.println("UserControllerTest.testingUserLoginEmailAndPasswordWithValidEmailInvalidPassword()");
		String email = "sujitmaharana111@gmail.com";
		String password = "sujit123";

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setEmail(email);
		loginRequestDTO.setPassword(password);

		when(userService.loginUserByEmailAndPassword(anyString(), anyString()))
				.thenThrow(new UserNotFoundException("Invalid password : " + password));

		String jsonInvalidLoginRequestDTO = this.objectMapper.writeValueAsString(loginRequestDTO);

		MvcResult result = mockMvc
							.perform(
									post("/user/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(jsonInvalidLoginRequestDTO)
									)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Invalid password : " + password))
				.andReturn();

		System.err.println("Status : " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());

	}

	// 15. testing userLoginEmailAndPassword with correct email and invalid password
	// throwing UserNotFoundException
	@Test
	void testingUserLoginEmailAndPasswordWithinValidEmailValidPassword() throws Exception {
		System.out.println("UserControllerTest.testingUserLoginEmailAndPasswordWithinValidEmailValidPassword()");
		String email = "sagarmaharana111@gmail.com";
		String password = "sujit123";

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setEmail(email);
		loginRequestDTO.setPassword(password);

		when(userService.loginUserByEmailAndPassword(anyString(), anyString()))
				.thenThrow(new UserNotFoundException("Invalid email : " + email));

		String jsonInvalidLoginRequestDTO = this.objectMapper.writeValueAsString(loginRequestDTO);

		MvcResult result = mockMvc
							.perform(
									post("/user/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(jsonInvalidLoginRequestDTO)
									)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Invalid email : " + email))
				.andReturn();

		System.err.println("Status : " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());

	}

	// 16.testing userLoginEmailAndPassword with valid email and password and user is not active
	@Test
	void testingUserLoginEmailAndPasswordWithValidDataAndInActiveUser() throws Exception {
		System.out.println("UserControllerTest.testingUserLoginEmailAndPasswordWithValidData()");
		String email = "sujitmaharana1111@gmail.com";
		String password = "sujit@123";

		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setEmail(email);
		loginRequestDTO.setPassword(password);

		userResponseDTO.setUserEmail(email);
		userResponseDTO.setUserPassword(password);

		String jsonLoginRequestDTO = this.objectMapper.writeValueAsString(loginRequestDTO);

		when(userService.loginUserByEmailAndPassword(anyString(), anyString()))
				.thenThrow(new RuntimeException("User is not active"));

		MvcResult result = mockMvc
							.perform(
									post("/user/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(jsonLoginRequestDTO)
									)
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.message").value("User is not active"))
				.andReturn();

		System.err.println("Status : " + result.getResponse().getStatus());
		System.err.println(result.getResponse().getContentAsString());
	}


	 //Test case for getAllUsers() in positive Scenario
    @Test
    void testGetAllUsers_Positive() throws Exception {
        UserResponseDTO mockUserResponse = new UserResponseDTO();
        mockUserResponse.setUserId("123");
        List<UserResponseDTO> users = Arrays.asList(mockUserResponse, mockUserResponse);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user/allUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].userId").value("123"));

        verify(userService, times(1)).getAllUsers();
    }

   //Test case for getAllUsers() in Negative Scenario
    
    @Test
    void testGetAllUsers_Negative() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error retrieving users"));

        mockMvc.perform(get("/user/allUsers"))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).getAllUsers();
    }
    
    //Test Case for GetAllUsers Active 
    @Test
    void testGetAllUsers_EmptyList_Positive() throws Exception {
      
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        
        mockMvc.perform(get("/user/allUsers"))
                .andDo(print())
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.size()").value(0)); 

       
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetAllUsers_Exception_Negative() throws Exception {
    	
    //throw exception using mock service (object)
    	
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Database error"));
        
        mockMvc.perform(get("/user/allUsers"))
                .andDo(print())
                .andExpect(status().isInternalServerError()); //throw an Internal Server Error

       
        verify(userService, times(1)).getAllUsers();
    }

    //Test Case for UpdatePassword in positive scenario.
    
    @Test
    void testUpdatePassword_Positive() throws Exception {
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO();
        requestDTO.setEmail("usha123@gmail.com"); 
        requestDTO.setNewPassword("newPass123"); 
        requestDTO.setConfirmPassword("newPass123");

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserEmail("usha123@gmail.com"); 
        responseDTO.setUserName("Usha sri");

        System.out.println(objectMapper.writeValueAsString(responseDTO));

   
        when(userService.updatePassword(any(ChangePasswordRequestDTO.class))).thenReturn(responseDTO);

     
        mockMvc.perform(post("/user/UpdatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("usha123@gmail.com"))
                .andExpect(jsonPath("$.userName").value("Usha sri"));

       
        verify(userService, times(1)).updatePassword(any(ChangePasswordRequestDTO.class));
    }

    //Test Case for UpdatePassword in Negative Scenario.
    
    @Test
    void testUpdatePassword_Negative() throws Exception {
     
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO();
        requestDTO.setEmail("usha123@gmail.com");
        requestDTO.setNewPassword("oldPass123");
        requestDTO.setConfirmPassword("newPass123");

      
        when(userService.updatePassword(any(ChangePasswordRequestDTO.class)))
                .thenThrow(new RuntimeException("Password update failed"));
   
      
        mockMvc.perform(post("/user/UpdatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError());

     
        verify(userService, times(1)).updatePassword(any(ChangePasswordRequestDTO.class));
    }

    @Test
    void testVerifyEmail_Success() throws Exception {
        EmailRequestDTO requestDTO = new EmailRequestDTO("Ushasri@gmail.com");
        Message responseMessage = new Message("Email verified successfully");

        when(userService.verifyUserByEmail(any(EmailRequestDTO.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/user/VerifyEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email verified successfully"));
    }
    @Test
    void testVerifyEmail_InvalidEmailFormat() throws Exception {
        EmailRequestDTO requestDTO = new EmailRequestDTO("invalid-email");

        mockMvc.perform(post("/user/VerifyEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest()) // Expecting 400 Bad Request
                .andExpect(jsonPath("$.email").value("Invalid email")) 
                .andDo(print()); 
    }
    // Test case for service failure
    @Test
    void testVerifyEmail_ServiceFailure() throws Exception {
        EmailRequestDTO requestDTO = new EmailRequestDTO("Ushasri@gmail.com");

        when(userService.verifyUserByEmail(any(EmailRequestDTO.class)))
                .thenThrow(new RuntimeException("Service failure occurred"));

        mockMvc.perform(post("/user/VerifyEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Service failure occurred"));
    }
	//17.verifyOTP api (posative scenario)
	@Test
	void testVerifyOTP() throws Exception
	{
		System.out.println("UserControllerTest.testVerifyOTP()");
		 VerifyOTPRequestDTO verifyOTPRequestDTO = new VerifyOTPRequestDTO();
		 verifyOTPRequestDTO.setEmail("sujitmaharana1111@gmail.com");
		 verifyOTPRequestDTO.setOtp("1234");
		 when(this.userService.verifyOTP(any(VerifyOTPRequestDTO.class))).thenReturn(new Message("OTP verified successfully"));
	     
		 String verifyOTPJSON = this.objectMapper.writeValueAsString(verifyOTPRequestDTO);
		 
		 
	     MvcResult result = mockMvc.perform(
	    		        post("/user/VerifyOTP")
	    		        .contentType(MediaType.APPLICATION_JSON)
	    		        .content(verifyOTPJSON)
	    		        )
	                   .andExpect(status().isOk())
		               .andExpect(jsonPath("$.message").value("OTP verified successfully"))
			           .andReturn();

	     System.err.println("Status : " + result.getResponse().getStatus());
		 System.err.println(result.getResponse().getContentAsString());
	
	}

}

