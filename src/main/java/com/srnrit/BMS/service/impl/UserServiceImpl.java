package com.srnrit.BMS.service.impl;

import org.springframework.stereotype.Service;

import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Override
	public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
		if(userRequestDTO!=null)
		{
			User user = DTOToEntity.userRequestDtoToUserEntity(userRequestDTO);
			if(user!=null)
			{
				
			}
			else {
				throw new RuntimeException("Something went Wrong");
			}
		}
		else {
			throw new RuntimeException("User Must Not be null");
		}
		return null;
	}

}
