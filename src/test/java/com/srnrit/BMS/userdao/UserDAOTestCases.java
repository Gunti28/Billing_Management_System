package com.srnrit.BMS.userdao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.srnrit.BMS.dao.impl.UserDaoImpl;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserAleadyExistException;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.repository.UserRepository;
import com.srnrit.BMS.service.impl.UserServiceImpl;
import com.srnrit.BMS.util.FileStorageProperties;

@ExtendWith(MockitoExtension.class)
public class UserDAOTestCases {
	
	@Mock
    private UserRepository userRepository;

    @Mock
    private FileStorageProperties fileStorageProperties;

    @InjectMocks
    private UserDaoImpl userDao;

    @InjectMocks
    private UserServiceImpl userService;
    
	
    //USERSAVE() METHOD IN USERDAO LAYER
//	@Test
//	void userSaveTest() 
//	{
//		User user = new User();
//		user.setUserName("venu");
//		user.setUserEmail("venu@gmail.com");
//		user.setUserPassword("Venu@!2334");
//		user.setUserGender("Male");
//		user.setUserPhone(8712339224L);
//		user.setTermsAndConditions(true);
//		
//		Optional<User> saveuser = userDAO.saveuser(user);
//		 assertTrue(saveuser.isPresent()); 
//		 System.out.println(saveuser.get());
//		
//		
//	} 
    
    	//FINDBYUSERID() USERDAO CLASS METHOD
 // Positive Test Case: Valid User ID with Existing User
//    @Test
//    public void testFindByUserId_ValidId_UserExists() {
//        // Arrange
//        String userId = "Uid_01";
//        User user = new User();
//        user.setUserId(userId);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        // Act
//        Optional<User> result = userDao.findByUserId(userId);
//
//        // Assert
//        assertTrue(result.isPresent(), "Expected user to be present");
//        assertEquals(userId, result.get().getUserId(), "User ID should match");
//    }
//
//    // Positive Test Case: Valid User ID with Non-Existing User
//    @Test
//    public void testFindByUserId_ValidId_UserDoesNotExist() {
//        // Arrange
//        String userId = "123";
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Act
//        Optional<User> result = userDao.findByUserId(userId);
//
//        // Assert
//        assertFalse(result.isPresent(), "Expected user to be absent");
//    }
//
//    // Negative Test Case: Null User ID
//    @Test
//    public void testFindByUserId_NullId() {
//        // Arrange
//        String userId = null;
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            userDao.findByUserId(userId);
//        });
//
//        assertEquals("UserId must not be null or blank", exception.getMessage());
//    }
//
//    // Negative Test Case: Blank User ID
//    @Test
//    public void testFindByUserId_BlankId() {
//        // Arrange
//        String userId = "   ";
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            userDao.findByUserId(userId);
//        });
//
//        assertEquals("UserId must not be null or blank", exception.getMessage());
//    }
//    
//    
           //FINDBYUSEREMAIL() IN USERDAO LAYER
    @Test
    public void testFindByUserEmail_Success() {
        // Arrange
        User user = new User();
        user.setUserEmail("venu@gmail.c");
        user.setActive(true); // Ensure the user is active

        when(userRepository.findByUserEmail("venu@gmail.c")).thenReturn(user);

        // Act
        Optional<User> result = userDao.findByUserEmail("venu@gmail.c");

        // Assert
        assertTrue(result.isPresent(), "Expected user to be present");
        assertEquals(user, result.get(), "Expected the returned user to match the mock user");
    }
//    
//    
//    	//FINDBYUSERPHONENUMBER() IN USERDAO LAYER
    @Test
    public void testFindByUserPhoneNumber_Success() {
        // Arrange
        User user = new User();
        user.setUserPhone(1234567890L);

        when(userRepository.findByUserPhone(user.getUserPhone())).thenReturn(user);

        // Act
        Optional<User> result = userDao.findByUserPhoneNumber(user.getUserPhone());

        // Assert
        assertTrue(result.isPresent(), "Expected user to be present");
        assertEquals(user, result.get(), "Expected the returned user to match the mock user");
    }
//    
    
    	//FETCHALLUSERS IN USERDAO LAYER
//    @Test
//    public void testFetchAllUser_Success() 
//    {
//    	User user=new User();
//    	
//        List<User> users = new ArrayList<>();
//        users.add(user);
//        when(userRepository.findAll()).thenReturn(users);
//
//        Optional<List<User>> result = userDao.fetchAlluser();
//
//        assertTrue(result.isPresent());
//        assertEquals(users, result.get());
//    }
    
    //DELETEUSERBYID() IN USERDAO LAYER
    @Test
    public void testDeleteUserById_ActiveUser() {
        // Arrange
        String userId = "Uid_021";
        User user = new User();
        user.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        Optional<String> result = userDao.deleteUserById(userId);

        // Assert
        assertTrue(result.isPresent(), "Expected result to be present");
        assertEquals("User deleted successfully with ID: " + userId, result.get(), "Expected success message");
        assertFalse(user.getActive(), "User should be inactive after deletion");
    }

    @Test
    public void testDeleteUserById_InactiveUser() {
        // Arrange
        String userId = "Uid_022";
        User user = new User();
        user.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<String> result = userDao.deleteUserById(userId);

        // Assert
        assertTrue(result.isPresent(), "Expected result to be present");
        assertEquals("User is not active.", result.get(), "Expected inactive user message");
    }

    @Test
    public void testDeleteUserById_UserNotFound() {
        // Arrange
        String userId = "Uid_023";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.deleteUserById(userId);
        });

        assertEquals("User does not exist with ID: " + userId, exception.getMessage());
    }

    @Test
    public void testDeleteUserById_NullUserId() {
        // Arrange
        String userId = null;

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.deleteUserById(userId);
        });

        assertEquals("Userid can't be null or blank.", exception.getMessage());
    }

    @Test
    public void testDeleteUserById_BlankUserId() {
        // Arrange
        String userId = "   ";

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.deleteUserById(userId);
        });

        assertEquals("Userid can't be null or blank.", exception.getMessage());
    }

    //EDITIMAGE() IN USERDAO LAYER
    
    
    //UPDATEUSERBYID() IN USERDAO LAYER
//    @Test
//    public void testUpdateByUserId_Success() {
//        // Arrange
//        String userId = "Uid_001";
//        User existingUser = new User();
//        existingUser.setActive(true);
//
//        User updatedUser = new User();
//        updatedUser.setUserEmail("newemail@example.com");
//        updatedUser.setUserPhone(1234567890L);
//        updatedUser.setUserName("New Name");
//        updatedUser.setUserPassword("NewPassword");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByUserEmail(updatedUser.getUserEmail())).thenReturn(null);
//        when(userRepository.findByUserPhone(updatedUser.getUserPhone())).thenReturn(null);
//
//        // Act
//        Optional<User> result = userDao.updateByUserId(updatedUser, userId);
//
//        // Assert
//        assertTrue(result.isPresent(), "Expected user to be present");
//
//        // Capture the argument passed to save
//        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//        verify(userRepository).save(userCaptor.capture());
//
//        // Verify the captured user
//        User savedUser = userCaptor.getValue();
//        assertEquals(updatedUser.getUserEmail(), savedUser.getUserEmail(), "Expected email to match");
//        assertEquals(updatedUser.getUserPhone(), savedUser.getUserPhone(), "Expected phone to match");
//        assertEquals(updatedUser.getUserName(), savedUser.getUserName(), "Expected name to match");
//        assertEquals(updatedUser.getUserPassword(), savedUser.getUserPassword(), "Expected password to match");
//    }
    @Test
    public void testUpdateByUserId_UserNotFound() {
        // Arrange
        String userId = "Uid_002";
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDao.updateByUserId(user, userId);
        });

        assertEquals("User not exist with id : " + userId, exception.getMessage());
    }
    
    @Test
    public void testUpdateByUserId_InactiveUser() {
        // Arrange
        String userId = "Uid_003";
        User existingUser = new User();
        existingUser.setActive(false);

        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.updateByUserId(user, userId);
        });

        assertEquals("user is not active", exception.getMessage());
    }

    @Test
    public void testUpdateByUserId_DuplicateEmail() {
        // Arrange
        String userId = "Uid_004";
        User existingUser = new User();
        existingUser.setActive(true);

        User user = new User();
        user.setUserEmail("duplicate@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUserEmail(user.getUserEmail())).thenReturn(new User());

        // Act & Assert
        UserAleadyExistException exception = assertThrows(UserAleadyExistException.class, () -> {
            userDao.updateByUserId(user, userId);
        });

        assertEquals("User already exist with email : " + user.getUserEmail(), exception.getMessage());
    }

    @Test
    public void testUpdateByUserId_DuplicatePhone() {
        // Arrange
        String userId = "Uid_005";
        User existingUser = new User();
        existingUser.setActive(true);

        User user = new User();
        user.setUserPhone(1234567890L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUserEmail(user.getUserEmail())).thenReturn(null);
        when(userRepository.findByUserPhone(user.getUserPhone())).thenReturn(new User());

        // Act & Assert
        UserAleadyExistException exception = assertThrows(UserAleadyExistException.class, () -> {
            userDao.updateByUserId(user, userId);
        });

        assertEquals("User already exist with phoneNumber" + user.getUserPhone(), exception.getMessage());
    }

    @Test
    public void testUpdateByUserId_NullUser() {
        // Arrange
        String userId = "Uid_006";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDao.updateByUserId(null, userId);
        });

        assertEquals("User can't be null!", exception.getMessage());
    }

    @Test
    public void testUpdateByUserId_NullOrBlankUserId() {
        // Arrange
        User user = new User();

        // Act & Assert for null userId
        UserNotFoundException exception1 = assertThrows(UserNotFoundException.class, () -> {
            userDao.updateByUserId(user, null);
        });

        assertEquals("User id can't be null or blank!", exception1.getMessage());

        // Act & Assert for blank userId
        UserNotFoundException exception2 = assertThrows(UserNotFoundException.class, () -> {
            userDao.updateByUserId(user, "   ");
        });

        assertEquals("User id can't be null or blank!", exception2.getMessage());
    }

	

}
