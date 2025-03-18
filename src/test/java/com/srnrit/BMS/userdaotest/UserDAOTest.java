package com.srnrit.BMS.userdaotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.impl.UserDaoImpl;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserAleadyExistException;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.repository.UserRepository;
import com.srnrit.BMS.util.FileStorageProperties;
import com.srnrit.BMS.util.idgenerator.ImageFileNameGenerator;

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
 
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("Uid_1");
        user.setUserEmail("test@example.com");
        user.setUserPhone(1234567890L);
        user.setUserPassword("Kavya@0502");
        user.setUserName("karunya");
        user.setActive(true);
        user.setUserProfileImage("default.png");
        user.setDateOfRegistration(LocalDateTime.now());
    }

    //1.USERSAVE() METHOD IN USERDAO LAYER
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
       
    	//2.DELETEBYUSERID() USERDAO CLASS METHOD IN USERDAO LAYER
    
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
    
    	//3.FINDBYUSERID() USERDAO CLASS METHOD
    @Test
    public void testFindByUserId_ValidUserId() {
        String userId = "123";
        User user = new User();
        user.setUserId(userId);
        user.setUserName("John Doe");        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));        
        Optional<User> result = userDao.findByUserId(userId);        
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByUserId_UserNotFound() {
        String userId = "nonExistentUserId";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());       
        Optional<User> result = userDao.findByUserId(userId);       
        assertFalse(result.isPresent());
    }
    
    @Test
    public void testFindByUserId_NullUserId() {
        String userId = null;       
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.findByUserId(userId);
        });       
        assertEquals("UserId must not be null or blank", exception.getMessage());
    }

    @Test
    public void testFindByUserId_BlankUserId() {
        String userId = "";     
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.findByUserId(userId);
        });        
        assertEquals("UserId must not be null or blank", exception.getMessage());
    }
    
         //4.FINDBYUSEREMAIL() IN USERDAO LAYER
    @Test
    public void testFindByUserEmail_ValidEmail_ActiveUser() {
        String userEmail = "activeUser@example.com";
        User user = new User();  // Using default constructor
        user.setUserEmail(userEmail);  // Setting email
        user.setUserName("John Doe");  // Setting name
        user.setActive(true);  // Setting active status
        when(userRepository.findByUserEmail(userEmail)).thenReturn(user);
        Optional<User> result = userDao.findByUserEmail(userEmail);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByUserEmail_ValidEmail_InactiveUser() {
        String userEmail = "inactiveUser@example.com";
        User user = new User();  // Using default constructor
        user.setUserEmail(userEmail);  // Setting email
        user.setUserEmail("Jane Doe");  // Setting name
        user.setActive(false);  // Setting inactive status
        when(userRepository.findByUserEmail(userEmail)).thenReturn(user);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.findByUserEmail(userEmail);
        });
        assertEquals("user is not active.", exception.getMessage());
    }

    @Test
    public void testFindByUserEmail_UserNotFound() {
        String userEmail = "nonExistentUser@example.com";
        when(userRepository.findByUserEmail(userEmail)).thenReturn(null); // User doesn't exist
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.findByUserEmail(userEmail);
        });
        assertEquals("user not exist with email : nonExistentUser@example.com", exception.getMessage());
    }

    @Test
    public void testFindByUserEmail_NullEmail() {
        String userEmail = null;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.findByUserEmail(userEmail);
        });
        assertEquals("user Email must not be null or blank!.", exception.getMessage());
    }

    @Test
    public void testFindByUserEmail_BlankEmail() {
        String userEmail = "    "; // Blank spaces
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.findByUserEmail(userEmail);
        });
        assertEquals("user Email must not be null or blank!.", exception.getMessage());
    }
    
    	//5.FINDBYUSERPHONENUMBER() IN USERDAO LAYER
    @Test
    public void testFindByUserPhoneNumber_ValidPhoneNumber_ExistingUser() {
        long userPhoneNumber = 1234567890L;
        User user = new User();
        user.setUserPhone(userPhoneNumber); // Set phone number
        user.setUserName("John Doe");
        when(userRepository.findByUserPhone(userPhoneNumber)).thenReturn(user);
        Optional<User> result = userDao.findByUserPhoneNumber(userPhoneNumber);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByUserPhoneNumber_ValidPhoneNumber_NoUserFound() {
        long userPhoneNumber = 9876543210L;
        when(userRepository.findByUserPhone(userPhoneNumber)).thenReturn(null); // No user found
        Optional<User> result = userDao.findByUserPhoneNumber(userPhoneNumber);
        assertFalse(result.isPresent()); // Optional should be empty
    }

    @Test
    public void testFindByUserPhoneNumber_InvalidPhoneNumber() {
        long userPhoneNumber = -1234567890L;
        when(userRepository.findByUserPhone(userPhoneNumber)).thenReturn(null); // No user found
        Optional<User> result = userDao.findByUserPhoneNumber(userPhoneNumber);
        assertFalse(result.isPresent()); // Optional should be empty
    }

    @Test
    public void testFindByUserPhoneNumber_ZeroPhoneNumber() {
        long userPhoneNumber = 0L;
        when(userRepository.findByUserPhone(userPhoneNumber)).thenReturn(null); // No user found
        Optional<User> result = userDao.findByUserPhoneNumber(userPhoneNumber);
        assertFalse(result.isPresent()); // Optional should be empty
    }

    @Test
    public void testFindByUserPhoneNumber_NullPhoneNumber() {
        Long userPhoneNumber = null;
        Optional<User> result = userDao.findByUserPhoneNumber(userPhoneNumber);
        assertFalse(result.isPresent()); // Ensure the Optional is empty
    }

    	//6.UPDATEUSERBYID() METHOD IN USERDAO LAYER
    @Test
    public void testUpdateByUserId_Success() 
    {
        String userId = "123";
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUserEmail("test@example.com");
        existingUser.setUserPhone(1234567890L);
        existingUser.setUserName("karunya");
        existingUser.setActive(true); // Ensure the user is active
        
        // New user details to update
        User updatedUser = new User();
        updatedUser.setUserEmail("kavya02@gmail.com");
        updatedUser.setUserPhone(9876543210L);
        updatedUser.setUserName("kavya");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));        
        when(userRepository.findByUserEmail(updatedUser.getUserEmail())).thenReturn(null);       
        when(userRepository.findByUserPhone(updatedUser.getUserPhone())).thenReturn(null);        
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);        
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));       
        Optional<User> result = userDao.updateByUserId(updatedUser, userId);       
        assertTrue(result.isPresent());
        User savedUser = userCaptor.getValue();
        assertEquals("kavya02@gmail.com", savedUser.getUserEmail());
        assertEquals(9876543210L, savedUser.getUserPhone());
        assertEquals("kavya", savedUser.getUserName());
    }

    @Test
    public void testUpdateByUserId_NullUser() {
        String userId = "123";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.updateByUserId(null, userId);
        });
        assertEquals("User can't be null!", exception.getMessage());
    }
    
    @Test
    public void testUpdateByUserById_NulluserId() {
        User user = new User();
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.updateByUserId(user, null);
        });
        assertEquals("User id can't be null or blank!", exception.getMessage());
    }
    
    @Test
    public void testUpdateByUserId_BlankUserId() {
        User user = new User();
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.updateByUserId(user, " ");
        });
        assertEquals("User id can't be null or blank!", exception.getMessage());
    }
    
    @Test
    public void testUpdateByUserId_UserNotFound() {
        String userId = "123";
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.updateByUserId(user, userId);
        });
        assertEquals("User not exist with id : "+userId, exception.getMessage());
    }
    
    @Test
    public void testUpdateByUserId_UserNotActive() {
        String userId = "123";
        User inactiveUser = new User();
        inactiveUser.setActive(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(inactiveUser));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.updateByUserId(new User(), userId);
        });
        assertEquals("user is not active", exception.getMessage());
    }
    
    @Test
    public void testUpdateByUserId_EmailAlreadyExists() 
    {
        String userId = "123";
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setActive(true);
        
        User updatedUser = new User();
        updatedUser.setUserEmail("duplicate@example.com");       
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));       
        when(userRepository.findByUserEmail(updatedUser.getUserEmail())).thenReturn(updatedUser); // Simulate existing email        
        UserAleadyExistException exception = assertThrows(UserAleadyExistException.class, () -> {
            userDao.updateByUserId(updatedUser, userId);
        });
        assertEquals("User already exists with email: " + updatedUser.getUserEmail(), exception.getMessage());
    }
    
    
    @Test
    public void testUpdateByUserId_PhoneNumberAlreadyExists() {
        // Arrange
        String userId = "123";
        
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setActive(true);
        User updatedUser = new User();        
        updatedUser.setUserPhone(9876543210L); // Phone number to update
        updatedUser.setUserEmail("duplicate@example.com"); 
        
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));	        
        when(userRepository.findByUserEmail(updatedUser.getUserEmail())).thenReturn(null); // No existing email        
        when(userRepository.findByUserPhone(updatedUser.getUserPhone())).thenReturn(updatedUser); // Simulate existing phone number        
        UserAleadyExistException exception = assertThrows(UserAleadyExistException.class, () -> {
            userDao.updateByUserId(updatedUser, userId);
        });
        assertEquals("User already exists with phonenumber: " + updatedUser.getUserPhone(), exception.getMessage());
    }
    
   
    	//7.FETCHALLUSERS() METHOD IN USERDAO LAYER
    @Test
    public void testFetchAllUser_Success() {
        User user1 = new User();
        User user2 = new User();
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);
        Optional<List<User>> result = userDao.fetchAlluser();
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testFetchAllUser_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        Optional<List<User>> result = userDao.fetchAlluser();
        assertFalse(result.isPresent());
    }

    @Test
    public void testFetchAllUser_NullResponse() {
        when(userRepository.findAll()).thenReturn(null);
        Optional<List<User>> result = userDao.fetchAlluser();
        assertFalse(result.isPresent());
    }

    	//8.CHANGE__PASSWORD() IN USERDAO LAYER
    @SuppressWarnings("unused")
	@Test
    public void testChangePassword_Success() {
        // Arrange
        String email = "test@example.com";
        String newPassword = "NewPass123";
        
        User user = new User();
        user.setUserEmail(email);
        user.setUserPassword("OldPass");
        user.setActive(true);  // Set active to avoid NullPointerException
        when(userRepository.findByUserEmail(email)).thenReturn(user);
        Optional<User> result = userDao.changePassword(email, newPassword);
        assertEquals(newPassword, user.getUserPassword());       
        verify(userRepository).save(user);
    }

    @Test
    public void testChangePassword_UserNotFound() 
    {
        String userEmail = "unknown@example.com";
        when(userRepository.findByUserEmail(userEmail)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.changePassword(userEmail, "NewPassword123");
        });
        assertEquals("user not exist with email : "+userEmail, exception.getMessage());
    }

    @Test
    public void testChangePassword_UserNotActive() 
    {
        String userEmail = "inactive@example.com";
        User inactiveUser = new User();
        inactiveUser.setUserEmail(userEmail);
        inactiveUser.setActive(false);
        when(userRepository.findByUserEmail(userEmail)).thenReturn(inactiveUser);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.changePassword(userEmail, "NewPassword123");
        });

        assertEquals("user is not active.", exception.getMessage());
    }
    
    
    		//EDITIMAGEBYID() IN USERDAO LAYER
    @Test
    public void testEditImage_NullUserId() {
        MultipartFile file = mock(MultipartFile.class);
        assertThrows(UserNotFoundException.class, () -> {
            userDao.editImage(file, null);
        }, "User ID must not be null or empty.");
    }
    
    @Test
    public void testEditImage_UserNotActive() 
    {
        String userId = "inactiveUserId";
        MultipartFile file = mock(MultipartFile.class);
        User inactiveUser = new User();
        inactiveUser.setUserId(userId);
        inactiveUser.setActive(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(inactiveUser));
        assertThrows(RuntimeException.class, () -> {
            userDao.editImage(file, userId);
        }, "User is not active");
    }

    	
    @Test
    public void testEditImage_UserNotUpdatedSuccessfully() 
    {
        String userId = "validUserId";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("newImage.png");
        User user = new User();
        user.setUserId(userId);
        user.setActive(true);
        user.setUserProfileImage("oldImage.png");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> {
            userDao.editImage(file, userId);
        },"User not updated successfully !");
        
    }
    
}   
