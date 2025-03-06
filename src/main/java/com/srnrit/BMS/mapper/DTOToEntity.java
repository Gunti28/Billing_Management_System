package com.srnrit.BMS.mapper;

import org.springframework.beans.BeanUtils;

import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.entity.User;

public class DTOToEntity {
	
	public static User userRequestDtoToUserEntity(UserRequestDTO dto)
	{
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		user.setUserPhone(Long.parseLong(dto.getUserPhone()));
		return user;
	}
	
	

}
