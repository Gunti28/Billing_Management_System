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

	@Override
	public String deleteUserById(String userId) {
		Optional<String> deleteUser = userDao.deleteUser(userId);
		return deleteUser.orElseThrow(()-> new RuntimeException("Something went wrong"));
	}

	@Override
	public UserResponseDTO updateUserById(UserRequestDTO userRequestDTO,String userId) 
	{
		if(userRequestDTO!=null)
		{
			if(userId!=null && !userId.isBlank())
			{
				User user = DTOToEntity.userRequestDtoToUserEntity(userRequestDTO);
				Optional<User> updateUser = this.userDao.updateByUserId(user, userId);
				if(updateUser.isPresent())
				{
					UserResponseDTO userResponseDTO = EntityToDTO.userEntityToUserResponseDTO(updateUser.get());
					if(userResponseDTO!=null)
					{
						return userResponseDTO;
					}
					else throw new RuntimeException("Something went wrong,try again some time");
				}
				else throw new RuntimeException("User Not Updated , Try After Some Time");
			}
			else throw new RuntimeException("Userid must not be null or blank");
		}
		else throw new RuntimeException("UserRequestDTO must not be null");
		
	}

	@Override
	public UserResponseDTO findUserById(String userId)
	{
		if(userId!=null &&  ! userId.isBlank())
		{
			Optional<User> byUserId = this.userDao.findByUserId(userId);
			if(byUserId.isPresent())
			{
				UserResponseDTO userResponseDTO = EntityToDTO.userEntityToUserResponseDTO(byUserId.get());
				if(userResponseDTO!=null)
				{
					return userResponseDTO;
				}
				else throw new RuntimeException("Something went wrong, Try again sometime");
			}
			else throw new RuntimeException("User Not Found with Id: "+userId);
		}
		else throw new RuntimeException("Userid must not be null or blank");
	}

	
	
	
	
	

	

}
