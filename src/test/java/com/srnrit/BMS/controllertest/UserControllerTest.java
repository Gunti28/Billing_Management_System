package com.srnrit.BMS.controllertest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srnrit.BMS.controller.UserController;
import com.srnrit.BMS.dto.ChangePasswordRequestDTO;
import com.srnrit.BMS.dto.EmailRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.service.UserService;
import com.srnrit.BMS.util.Message;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDTO mockUserResponse;

    @BeforeEach
    void setUp() {
        mockUserResponse = new UserResponseDTO();
        mockUserResponse.setUserId("02");
        mockUserResponse.setUserName("Usha sri");
        mockUserResponse.setUserEmail("usha123@gmail.com");
        mockUserResponse.setUserPhone(9876543870L);
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
}