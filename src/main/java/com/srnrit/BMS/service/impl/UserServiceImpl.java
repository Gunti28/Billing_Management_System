package com.srnrit.BMS.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.dto.UpdateUserRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UnSupportedFileTypeException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.UserService;
import com.srnrit.BMS.util.FileStorageProperties;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
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
		Optional<String> deleteUser = userDao.deleteUserById(userId);
		return deleteUser.orElseThrow(()-> new RuntimeException("Something went wrong"));
	}

	@Override
	public UserResponseDTO updateUserById(UpdateUserRequestDTO updateUserRequestDTO,String userId) 
	{
		if(updateUserRequestDTO!=null)
		{
			if(userId!=null && !userId.isBlank())
			{
				User user = DTOToEntity.userUpdateRequestDtoToUserEntity(updateUserRequestDTO);
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

	@Override
	public UserResponseDTO loginUserByEmailAndPassword(String email,String password) {
		if((email!=null &&  ! email.isBlank()) && (password!=null && ! password.isBlank()))
		{
			if(email.matches("^[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$"))
			{
				if(password.length()>6)
				{
					Optional<User> userFetchedByEmailAndPassword = this.userDao.loginByEmailAndPassword(email,password);
					if(userFetchedByEmailAndPassword.isPresent())
					{
						UserResponseDTO userResponseDTO = EntityToDTO.userEntityToUserResponseDTO(userFetchedByEmailAndPassword.get());
						return userResponseDTO;
						
					}
					else throw new RuntimeException("User is not active");
				}
				else throw new IllegalArgumentException("Password Must be at least 6 characters");
			}
			else throw new IllegalArgumentException("Invalid Email Format");
		}
		else throw new IllegalArgumentException("Email and Password must not be null or blank");
	}

	@Override
	public UserResponseDTO editUserImage(MultipartFile file, String userId) {
		if(userId!=null && !userId.isBlank())
		{
			if(file!=null)
			{
				System.out.println(file.getSize());
				long maxSize=fileStorageProperties.getMaxFileSize();
				if(file.getSize()<=maxSize)
				{
					String contentType=file.getContentType();
					if(contentType.startsWith("image/"))
					{
						String fileNameExtension=getFileExtension(file.getOriginalFilename());

						if(Arrays.asList("jpg","jpeg","png","git","tiff","bmp","svg","webp","heif").contains(fileNameExtension))
						{
							Optional<User> optionalUser = this.userDao.editImage(file, userId);
							if(optionalUser.isPresent())
							{
								UserResponseDTO userResponseDTO = EntityToDTO.userEntityToUserResponseDTO(optionalUser.get());
								return userResponseDTO;
							}
							else throw new RuntimeException("User image Not Updated ! Try again some time.");
						}
						else throw new UnSupportedFileTypeException("Invalid File Extension.");
					}
					else throw new UnSupportedFileTypeException("Invalid File Type ! Only images are allowed");
				}
				else throw new RuntimeException("File size exceeds maximum limit! Supported file size "+maxSize);
			}
			else throw new RuntimeException("File must not be null");
		}
		else throw new RuntimeException("Userid Must not be null or blank");
	}
	
	private String getFileExtension(String fileName)
	{
		int dotIndex=fileName.lastIndexOf(".");
		if(dotIndex==-1)
		{
			throw new UnSupportedFileTypeException("Invalid File");
		}
	    return fileName.substring(dotIndex+1);
	}

	@Override
	public List<UserResponseDTO> getAllUsers()
	{
		Optional<List<User>> optionalUsers = this.userDao.fetchAlluser();
		if(optionalUsers.isPresent())
		{
			List<User> listOfUser=optionalUsers.get();
			if(listOfUser!=null)
			{
				if(listOfUser.size()>0)
				{
					List<UserResponseDTO> listOfUserResponseDTO=new ArrayList<>();
					for(User user:listOfUser)
					{
						UserResponseDTO userResponseDTO = EntityToDTO.userEntityToUserResponseDTO(user);
						listOfUserResponseDTO.add(userResponseDTO);
					}
					return listOfUserResponseDTO;
				}
			}
		}
		throw new RuntimeException("No User is Present");
		
	}


	

}