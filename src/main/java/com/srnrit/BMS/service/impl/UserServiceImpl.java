package com.srnrit.BMS.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
		if(userRequestDTO!=null)
		{
			User user = DTOToEntity.userRequestDtoToUserEntity(userRequestDTO);
			if(user!=null)
			{
				Optional<User> saveuser = userDao.saveuser(user);
				if(saveuser.isPresent())
				{
					UserResponseDTO userResponseDTO = EntityToDTO.userEntityToUserResponseDTO(saveuser.get());
					if(userResponseDTO!=null)
					{
						return userResponseDTO;
					}
					else
					{
						throw new UserNotcreatedException("UserResponseDTO must not be null");
					}
				}
				else
				{
					throw new UserNotcreatedException("User Not created");
				}
			}
			else {
				throw new RuntimeException("Something went wrong by converting DTO To User");
			}
		}
		else {
			throw new RuntimeException("UserRequestDTO must not be null");
		}
		
	}

}
