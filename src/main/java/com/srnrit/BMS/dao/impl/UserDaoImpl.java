package com.srnrit.BMS.dao.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserAleadyExistException;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.repository.UserRepository;
import com.srnrit.BMS.util.FileStorageProperties;
import com.srnrit.BMS.util.idgenerator.ImageFileNameGenerator;

@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	UserRepository userRepository;
	
	 @Autowired
	    private FileStorageProperties fileStorageProperties;
	
	@Override
	public Optional<User> saveuser(User user) {
		if(user!=null) 
		{
			User byUserEmail = userRepository.findByUserEmail(user.getUserEmail());
			if(byUserEmail==null) 
			{
				User byUserPhonenumber = userRepository.findByUserPhone(user.getUserPhone());
				if(byUserPhonenumber ==null) 
				{
					user.setActive(true);
					user.setUserProfileImage("default.png");
					user.setDateOfRegistration(LocalDateTime.now());
					User saveUser = userRepository.save(user);
		
					return  saveUser!=null? Optional.of(saveUser) :Optional.empty();
				}
				else 
				{
					throw new UserNotcreatedException("User Already Existing with this PhoneNumber "+user.getUserPhone());
				}
			}
			else {
				throw new UserNotcreatedException("User Already Existing with this Email "+user.getUserEmail());
			}
		}
		else {
			throw new UserNotFoundException("User can not be null.");
		}
	}
	
	@Override
	public Optional<String> deleteUserById(String userId) {
    	
        // Validate the User by userId
        if (userId != null && !userId.isBlank()) {
        	
        	Optional<User> byId = userRepository.findById(userId);
	        
	        if (byId.isPresent()) 
	        {
	            User user = byId.get();
	            if (user.getActive()) //isActive is true
	            {
	               user.setActive(false); 
	               return userRepository.save(user) !=null?Optional.of("User deleted successfully with ID: " + userId):Optional.empty();
	            }
	            else return Optional.of("User is not active.");
	          
	        } else  throw new UserNotFoundException("User does not exist with ID: " + userId);
	        
        }
        else throw new UserNotFoundException("Userid can't be null or blank.");
    }

	@Override
	public Optional<User> findByUserId(String userId) {
		
		if(userId!=null && !userId.isBlank())
		{
			Optional<User> optionaluser = userRepository.findById(userId);
			return optionaluser.isPresent() ? Optional.of(optionaluser.get()) : Optional.empty();
		}
		throw new RuntimeException("UserId must not be null or blank");
	}

	
	@Override
	public Optional<User> updateByUserId(User user,String userId) {
		if(user!=null)
		{
			if(userId!=null && !userId.isBlank()) 
			{
				Optional<User> byId = userRepository.findById(userId);
				
				if(byId.isPresent()) 
				{
					User oldUser = byId.get();
					if(userRepository.findByUserEmail(user.getUserEmail()) == null ) 
					{
						
						if(userRepository.findByUserPhone(user.getUserPhone()) == null)
						{
							
							
							oldUser.setUserEmail(user.getUserEmail());
							oldUser.setUserName(user.getUserName());
							oldUser.setUserPassword(user.getUserPassword());
							oldUser.setUserPhone(user.getUserPhone());
							
							oldUser = userRepository.save(oldUser);
							
							return oldUser!=null?Optional.of(oldUser):Optional.empty();
							
						}
						else throw new UserAleadyExistException("User already exist with phoneNumber"+user.getUserPhone());
						
					}
					else throw new UserAleadyExistException("User already exist with email : "+user.getUserEmail());	
						
				}
				else throw new UserNotFoundException("User not exist with id : "+userId);
				
			}
			else throw new UserNotFoundException("User id can't be null or blank!"); 
			
		}
		else throw new RuntimeException("User can't be null!"); 		
	}

	@Override
	public Optional<User> findByUserEmail(String userEmail) {
		if(userEmail!=null && !userEmail.isBlank())
		{
			User byUserEmail = userRepository.findByUserEmail(userEmail);
			return byUserEmail!=null ? Optional.of(byUserEmail) : Optional.empty();
		}
		else throw new RuntimeException("user Email must not be null or blank!.");	
	}

	@Override
	public Optional<User> findByUserPhoneNumber(long userPhoneNumber) 
	{
		User byUserPhone = userRepository.findByUserPhone(userPhoneNumber);
		return byUserPhone!=null?Optional.of(byUserPhone):Optional.empty();
	}

	@Override
	public Optional<User> loginByEmailAndPassword(String userEmail, String userPassword) 
	{
		User user = userRepository.findByUserEmailAndUserPassword(userEmail, userPassword);
		
		if(user!=null)
		{
			if(user.getActive())
			{
				return Optional.of(user);
			}
			else throw new RuntimeException("user is not active.");
		}
		return Optional.empty();
		
		
			
	}


    @Override
    public Optional<User> editImage(MultipartFile file, String userId) {
        System.out.println("update the profile.");
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }
        
        long maxSize = fileStorageProperties.getMaxFileSize();// 10MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10MB.");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type. Only images are allowed.");
        }
        
        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        if (!Arrays.asList("jpg", "jpeg", "png", "gif").contains(filenameExtension.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file extension. Only .jpg, .jpeg, .png, .gif allowed.");
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required.");
        }
        
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new IllegalArgumentException("User not found.");
        }
        
        if (isVirus(file)) {
            throw new SecurityException("File contains a virus.");
        }
        
        String fileName = ImageFileNameGenerator.getNewFileName(file.getOriginalFilename());
        String targetDirectory = fileStorageProperties.getImageStoragePath();
        Path path = Paths.get(targetDirectory);
        Path targetLocation = path.resolve(fileName);
        
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            
            Optional<User> userOptional = userRepository.findById(userId);
            
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String oldImageFileName = user.getUserProfileImage();
                user.setUserProfileImage(fileName);
                User updatedUser = userRepository.save(user);
                
                if (updatedUser != null) {
                    if (oldImageFileName != null && !oldImageFileName.isEmpty()) {
                        File oldFile = new File(targetDirectory.concat(File.separator).concat(oldImageFileName));
                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                    }
                    
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    return Optional.of(updatedUser);
                }
            } else throw new UserNotFoundException("User not found with ID: " + userId);
        } catch (IOException e) {
            throw new RuntimeException("Image not stored successfully!", e);
        }
        return Optional.empty();
    }
    
    private boolean isVirus(MultipartFile file) {
        return false;
    }
}

	
	

