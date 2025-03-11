package com.srnrit.BMS.userdaotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.impl.UserDaoImpl;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.repository.UserRepository;
import com.srnrit.BMS.util.FileStorageProperties;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {
	
	@Mock
    private UserRepository userRepository;

    @Mock
    private FileStorageProperties fileStorageProperties;
    
    @Mock
    private MultipartFile file;
    
    @InjectMocks
    private UserDaoImpl userDao;

    private User user;
    
	
    //USERSAVE() METHOD IN USERDAO LAYER
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("Uid_1");
        user.setUserEmail("test@example.com");
        user.setUserPhone(1234567890L);
        user.setUserPassword("password");
        user.setUserName("Test User");
        user.setActive(true);
        user.setUserProfileImage("default.png");
        user.setDateOfRegistration(LocalDateTime.now());
    }

    @Test
    void testSaveUser_Success() {
        when(userRepository.findByUserEmail(user.getUserEmail())).thenReturn(null);
        when(userRepository.findByUserPhone(user.getUserPhone())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userDao.saveuser(user);
        
        assertTrue(result.isPresent());
        assertEquals(user.getUserEmail(), result.get().getUserEmail());
    }

    @Test
    void testSaveUser_ExistingEmail() {
        when(userRepository.findByUserEmail(user.getUserEmail())).thenReturn(user);
        
        assertThrows(UserNotcreatedException.class, () -> userDao.saveuser(user));
    }
    
    @Test
    void testSaveUser_ExistingPhoneNumber() {
        when(userRepository.findByUserEmail(user.getUserEmail())).thenReturn(null);
        when(userRepository.findByUserPhone(user.getUserPhone())).thenReturn(user);
        
        assertThrows(UserNotcreatedException.class, () -> userDao.saveuser(user));
    }
    
    @Test
    void testSaveUser_NullUser() {
        assertThrows(UserNotFoundException.class, () -> userDao.saveuser(null));
    }
      
    
    	//FINDBYUSERID() USERDAO CLASS METHOD
    @Test
    void testDeleteUserById_Success() {
        User user = new User();  
        user.setUserId("Uid_1");
        user.setActive(true);
        when(userRepository.findById("Uid_1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user); 
        Optional<String> result = userDao.deleteUserById("Uid_1");
        assertTrue(result.isPresent());
        assertEquals("User deleted successfully with ID: Uid_1", result.get());
        verify(userRepository, times(1)).findById("Uid_1");
        verify(userRepository, times(1)).save(user);
    }

    
    @Test
    void testDeleteUserById_UserNotFound() {
        when(userRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userDao.deleteUserById("123"));
    }
    
    @Test
    void testDeleteUserById_UserAlreadyInactive() {
        user.setActive(false);
        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        Optional<String> result = userDao.deleteUserById("123");
        
        assertTrue(result.isPresent());
        assertEquals("User is not active.", result.get());
    }
    
    @Test
    void testDeleteUserById_NullOrBlankId() {
        assertThrows(UserNotFoundException.class, () -> userDao.deleteUserById(null));
        assertThrows(UserNotFoundException.class, () -> userDao.deleteUserById(""));
    }
    
    
           //FINDBYUSEREMAIL() IN USERDAO LAYER
    
    
    
    
    		//CHANGE__PASSWORD() IN USERDAO LAYER
    


}
