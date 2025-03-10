package com.srnrit.BMS.userdao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.srnrit.BMS.dao.impl.UserDaoImpl;
import com.srnrit.BMS.entity.User;
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
    

  
    //EDITIMAGE() IN USERDAO LAYER

	

}
