package com.srnrit.BMS.dao.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserAleadyExistException;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.repository.UserRepository;

@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	UserRepository userRepository;
	
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
	public Optional<String> deleteUser(String userId) {
    	
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
	            else
	            {
	                return Optional.of("User is not active.");
	            }
	        } else 
	        {
	            throw new UserNotFoundException("User does not exist with ID: " + userId);
	        }
        }
        else 
        {
        	throw new UserNotFoundException("Userid can't be null or blank.");
        	

        }
        else throw new UserNotFoundException("User can't be null.");
  
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

	
	@SuppressWarnings("unused")
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
	
	
}
