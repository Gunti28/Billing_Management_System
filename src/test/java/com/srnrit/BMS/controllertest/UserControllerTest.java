package com.srnrit.BMS.controllertest;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.controller.UserController;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.service.UserService;

@ExtendWith(MockitoExtension.class) // Enables Mockito
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private UserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@InjectMocks
	private UserController userController;
	
	
	private UserRequestDTO userRequestDTO;
	private UserResponseDTO userResponseDTO;
	
	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this); 	
		userRequestDTO.setTermsAndConditions(true);
		userRequestDTO.setUserEmail("sujit456@gmail.com");
		userRequestDTO.setUserGender("Male");
		userRequestDTO.setUserName("Sujit Maharana");
		userRequestDTO.setUserPassword("sujit123");
		userRequestDTO.setUserPhone("6370270394");
		
		
		BeanUtils.copyProperties(userRequestDTO, userResponseDTO);
	}
	
	
	
	
	
	@Test
	void testCreateUser() throws Exception
	{
		when(userService.saveUser((UserRequestDTO) any(UserRequestDTO.class))).thenReturn(userResponseDTO);
	    
		mockMvc.perform(
				         post("/create")
				        .contentType(MediaType.APPLICATION_JSON)
				        .content(objectMapper.writeValueAsString(userResponseDTO))) //convert DTO to JSON
				        .andExpect(status().isOk())
				        .andExpect(jsonPath("$.userName").value("Sujit Maharana"))
				        .andExpect(jsonPath("$.userEmail").value("sujit456@gmail.com"))
				        .andExpect(jsonPath("$.userGender").value("Male"))
                        .andExpect(jsonPath("$.termsAndConditions").value(true))
                        .andExpect(jsonPath("$.userPassword").value("sujit123"))
                        .andExpect(jsonPath("$.userPhone").value(6370270394L))
                        .andExpect(jsonPath("$.userProfileImage").value("default.png"));
		       
	
	}
	
	
	
	

}
