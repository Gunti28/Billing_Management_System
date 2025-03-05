package com.srnrit.BMS.mapper;

import org.springframework.beans.BeanUtils;

import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.entity.User;

public class EntityToDTO {
	
	public static UserResponseDTO userEntityToUserResponseDTO(User user)
	{
		UserResponseDTO dto = new UserResponseDTO();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}

}
