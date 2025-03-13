package com.srnrit.BMS.dao.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
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
public class UserDaoImpl implements UserDao 
{
	 @Autowired
     private UserRepository userRepository;
	
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
				else throw new UserNotcreatedException("User Already Existing with this PhoneNumber "+user.getUserPhone());			
			}
			else throw new UserNotcreatedException("User Already Existing with this Email "+user.getUserEmail());	
		}
		else throw new UserNotFoundException("User can not be null.");	
	}
	
	@Override
	public Optional<String> deleteUserById(String userId) 
	{
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
					if(oldUser.getActive())
					{
						 if(user.getUserEmail()!=null && !user.getUserEmail().isBlank())
						 {
							if(!user.getUserEmail().equals(oldUser.getUserEmail()))
							{
								
							    if (userRepository.findByUserEmail(user.getUserEmail())!=null) 
							   {
							        throw new UserAleadyExistException("User already exists with email: " + user.getUserEmail());
							   }
							    oldUser.setUserEmail(user.getUserEmail());

							}
						 }
						 else throw new RuntimeException("user email must not be null or blank!.");
						 
						 String newPhonNo=String.valueOf(user.getUserPhone());
				
						 if(user.getUserPhone()!=null && (newPhonNo.length()==10 && !newPhonNo.startsWith("0")) )
						 {
							 if(!user.getUserPhone().equals(oldUser.getUserPhone())) 
							 {
								 if(userRepository.findByUserPhone(user.getUserPhone())!=null)
								 {
									 throw new UserAleadyExistException("User already exists with email: " + user.getUserEmail());
								 }
								 oldUser.setUserPhone(user.getUserPhone());
							 }
						 }
						 else throw new RuntimeException("user phoneNo must not be null and not start with '0' and length must be 10 digits.");
						 if(user.getUserName()!=null && !user.getUserName().isBlank())
						 {
							 if(!user.getUserName().equals(oldUser.getUserName()))
							      oldUser.setUserName(user.getUserName());
                               				
						 }
						 else throw new RuntimeException("user name must be null or blank!.");
						 oldUser = userRepository.save(oldUser);
						 return oldUser != null ? Optional.of(oldUser) : Optional.empty();
					}
					else throw new RuntimeException("user is not active");			
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
			if(byUserEmail!=null)
			{
				if(byUserEmail.getActive())
				{
					return  Optional.of(byUserEmail);
				}
				else throw new RuntimeException("user is not active.");	
			}
			else throw new UserNotFoundException("user not exist with email : "+userEmail);
		}
		else throw new RuntimeException("user Email must not be null or blank!.");	
	}

	@Override
	public Optional<User> findByUserPhoneNumber(Long userPhoneNumber) 
	{
		User byUserPhone = userRepository.findByUserPhone(userPhoneNumber);
		return byUserPhone!=null?Optional.of(byUserPhone):Optional.empty();
	}

	@Override
	public Optional<User> loginByEmailAndPassword(String userEmail, String userPassword) 
	{
		User user=null;
		user = userRepository.findByUserEmail(userEmail);
		if(user!=null)
		{
			user=userRepository.findByUserPassword(userPassword);
			if(user!=null)
			{
				return user.getActive() ? Optional.of(user):Optional.empty();	
			}
			else throw new RuntimeException("user not exists with password :"+userPassword);
		}
		else throw new RuntimeException("user not exists with email :"+userEmail);			
	}

	@Override
	public Optional<User> editImage(MultipartFile file, String userId) 
	{
	    // Validate user existence
	    Optional<User> userOptional = userRepository.findById(userId);
	    if (!userOptional.isPresent() ) 
	    	throw new UserNotFoundException("User does not exist with ID: " + userId);
	    // Get the user
	    User user = userOptional.get();
	    if(!user.getActive())
	    	throw new RuntimeException("User is not active ");
	    //getting old profile image name
	    String oldImageFileName=user.getUserProfileImage();
	    // Generate a new file name
	    String fileName = ImageFileNameGenerator.getNewFileName(file.getOriginalFilename());	    
	    // Update user profile image in db
	    user.setUserProfileImage(fileName);
	    User updatedUser = userRepository.save(user); //update image in db    
	  try 
	  { 
		    if(updatedUser != null)
		    {
		    	//now update image in local driver		    	
		    	String  targetDirectory=this.fileStorageProperties.getImageStoragePath();		 	  
		    	//create directories if not exist
		 		Path path = Paths.get(targetDirectory);		 		
		 		if(!Files.exists(path))
				{
					Files.createDirectories(path);
				}		 		
		 		//save the file with new image file name
		 		Path targetLocation= path.resolve(fileName);
		 		//for deleting purpose
		 		//start
		 		File oldFile = new File(targetDirectory.concat(oldImageFileName));
		 		
		 		 if(oldFile.exists() && !oldImageFileName.equals("default.png"))
				     oldFile.delete();
				 //end
	  
		 		//storing new image in the local driver 
		 		long copied = Files.copy(file.getInputStream(), targetLocation);
		 		
		 		return copied > 0 ? Optional.of(updatedUser) : Optional.empty();	
		    }
		    else 	throw new RuntimeException("User not updated successfully !");
	  } 
	  catch (Exception e) 
	  {
		 throw new RuntimeException(e.getMessage());
	  }  
	    
	}

	@Override
	public Optional<List<User>> fetchAlluser() 
	{
		List<User> allUsers = userRepository.findAll();
		return allUsers!=null && allUsers.size() > 0 ?Optional.of(allUsers):Optional.empty();
	}

	@Override
	public Optional<User> changePassword(String userEmail, String newPassword) 
	{
			  Optional<User> byUserEmail = this.findByUserEmail(userEmail);
			  if(byUserEmail.isPresent())
			  {
				  User user = byUserEmail.get();
				  if(user.getActive())
				  {
					  user.setUserPassword(newPassword);
					  User updatePassword = userRepository.save(user);
					  return updatePassword!=null?Optional.of(updatePassword):Optional.empty();
				  }
				  else throw new RuntimeException("User is not active");  
			  }
			  else throw new RuntimeException("User not exist with email "+userEmail);	
	}
}	

	

