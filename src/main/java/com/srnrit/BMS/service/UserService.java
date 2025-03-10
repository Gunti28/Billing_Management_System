package com.srnrit.BMS.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import com.srnrit.BMS.dto.UpdateUserRequestDTO;

import com.srnrit.BMS.dto.EmailRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.dto.VerifyOTPRequestDTO;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.util.Message;

public interface UserService {
	UserResponseDTO saveUser(UserRequestDTO userRequestDTO);
	String deleteUserById(String userId);
	UserResponseDTO updateUserById(UpdateUserRequestDTO updateUserRequestDTO, String userId);
	UserResponseDTO findUserById(String userId);
	UserResponseDTO loginUserByEmailAndPassword(String email,String password);
	UserResponseDTO editUserImage(MultipartFile file,String userId);
	List<UserResponseDTO> getAllUsers();
	UserResponseDTO updatePassword(String userEmail,String newPassword);
	Message verifyUserByEmail(EmailRequestDTO emailRequestDTO);
	Message verifyOTP(VerifyOTPRequestDTO verifyOTPRequestDTO);
}

