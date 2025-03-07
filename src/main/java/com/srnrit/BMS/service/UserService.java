package com.srnrit.BMS.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;

public interface UserService {
	UserResponseDTO saveUser(UserRequestDTO userRequestDTO);
	String deleteUserById(String userId);
	UserResponseDTO updateUserById(UserRequestDTO userRequestDTO, String userId);
	UserResponseDTO findUserById(String userId);
	UserResponseDTO loginUserByEmailAndPassword(String email,String password);
	UserResponseDTO editUserImage(MultipartFile file,String userId);
<<<<<<< HEAD
	List<UserResponseDTO> getAllUsers();
}
=======
}
>>>>>>> 4d4b32e18ab59029a3a5ab25db205055260b1ee4
