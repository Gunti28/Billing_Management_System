package com.srnrit.BMS.service;

import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;

public interface UserService {
	UserResponseDTO saveUser(UserRequestDTO userRequestDTO);
	String deleteUserById(String userId);
	UserResponseDTO updateUserById(UserRequestDTO userRequestDTO, String userId);
	UserResponseDTO findUserById(String userId);
	
}
