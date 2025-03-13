package com.srnrit.BMS.userservicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.security.CryptoPrimitive;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.dto.ChangePasswordRequestDTO;
import com.srnrit.BMS.dto.EmailRequestDTO;
import com.srnrit.BMS.dto.UpdateUserRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.dto.VerifyOTPRequestDTO;
import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.impl.UserServiceImpl;
import com.srnrit.BMS.util.FileStorageProperties;
import com.srnrit.BMS.util.OTPOperation;

@ExtendWith(MockitoExtension.class)
public class UserServiceTestCases {

	@Mock
	private UserDao userDao;

	@Mock
	private OTPOperation otpOperation;

	@Mock
	private FileStorageProperties fileStorageProperties;

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	private User user;
	private UserRequestDTO userRequestDTO;
	private UserResponseDTO userResponseDTO;
	private UpdateUserRequestDTO updateUserRequestDTO;
	private ChangePasswordRequestDTO changePasswordRequestDTO;
	private EmailRequestDTO emailRequestDTO;
	private VerifyOTPRequestDTO verifyOTPRequestDTO;

	
	 private MockMultipartFile validImage;
	 
	@BeforeEach
	void setUp() 
	{
		MockitoAnnotations.openMocks(this);

		userRequestDTO = new UserRequestDTO();
		userRequestDTO.setTermsAndConditions(true);
		userRequestDTO.setUserEmail("venu@gmail.com");
		userRequestDTO.setUserGender("Male");
		userRequestDTO.setUserName("venu");
		userRequestDTO.setUserPassword("venu1234");
		userRequestDTO.setUserPhone("7993494058");

		user=new User();
		BeanUtils.copyProperties(userRequestDTO, user);
		
		user.setUserId("Uid_01");
		user.setUserProfileImage("default.png");
		user.setActive(true);
		user.setUserPhone(7993494058l);
		
		
	    userResponseDTO = new UserResponseDTO();
		BeanUtils.copyProperties(user,userResponseDTO);
		userResponseDTO.setUserPhone(Long.parseLong("7993494058"));
		
		updateUserRequestDTO=new UpdateUserRequestDTO();
		updateUserRequestDTO.setUserEmail("venu@gmail.com");
		updateUserRequestDTO.setUserName("venu");
		updateUserRequestDTO.setUserPhone("7993494058");
		
		BeanUtils.copyProperties(updateUserRequestDTO, user);
		validImage=new  MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[104]);
		
		
		
		
	}
	
//================  SAVEUSER METHOD TEST CASES ==================
	
	// 1. Test case for saveUser() Success
    @Test
	void testSaveUser_Success()
    {
    	when(userDao.saveuser(any(User.class))).thenReturn(Optional.of(user));
    	UserResponseDTO userResponseDTO = userServiceImpl.saveUser(userRequestDTO);
    	assertEquals("venu@gmail.com", userResponseDTO.getUserEmail());
	}
 
	
    // 2. Test case for SaveUser()_Failure Passing null inplace of UserRequestDTO
	@Test
	void testSaveUser_Failure()
	{
		Exception exception = assertThrows(RuntimeException.class, ()->userServiceImpl.saveUser(null));
		assertEquals("UserRequestDTO must not be null", exception.getMessage());
		System.err.println(exception.getMessage());
	}
	
 	// 3.Test case for saveUser() DTOTOEntityConvertion fails i.e passing directly null to the static converter method of DTOTOEntity class  
	@Test
	void testSaveUser_DTOTOEntityConvertionFails()
	{
		try(MockedStatic<DTOToEntity> mockedConverter=mockStatic(DTOToEntity.class))
		{
			mockedConverter.when(()->DTOToEntity.userRequestDtoToUserEntity(any(UserRequestDTO.class))).thenReturn(null);
			
			Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.saveUser(userRequestDTO));
			assertEquals("Something went wrong by converting DTO To User", exception.getMessage());
			System.err.println(exception.getMessage());
		}
	}

	// 4. Test Case for If userDao.saveuser(user) returns Optional.empty(), it means the user wasn't saved.
	@Test
	void testSaveUser_FailsToSaveUser()
	{
		when(userDao.saveuser(any(User.class))).thenReturn(Optional.empty());
		Exception exception=assertThrows(UserNotcreatedException.class, ()->userServiceImpl.saveUser(userRequestDTO));
		assertEquals("User Not created", exception.getMessage());
		System.err.println(exception.getMessage());
	}

	// 5. Test Case for saveUser() If EntityToDTO.userEntityToUserResponseDTO() returns null, an exception should be thrown.
	@Test
	void testSaveUser_EntityToDTOConversionFails()
	{
		when(userDao.saveuser(any(User.class))).thenReturn(Optional.of(user));
		
		try(MockedStatic<EntityToDTO> mockedConverter=mockStatic(EntityToDTO.class))
		{
			mockedConverter.when(()->EntityToDTO.userEntityToUserResponseDTO(user)).thenReturn(null);
			
			Exception e=assertThrows(UserNotcreatedException.class, ()->userServiceImpl.saveUser(userRequestDTO));
			assertEquals("UserResponseDTO must not be null", e.getMessage());
			System.err.println(e.getMessage());
		}
	}
	
	// 6.  Test case for  User Already Exists
	@Test
	void testSaveUser_UserAlreadyExists()
	{
		//when(userDao.findByUserEmail(userRequestDTO.getUserEmail())).thenReturn(Optional.of(user));
		
		Exception e=assertThrows(RuntimeException.class, ()->userServiceImpl.saveUser(userRequestDTO));
		assertEquals("User Not created", e.getMessage());
		System.err.println(e.getMessage());
	}


//================ DELETE METHOD TEST CASES =================


	// 7. success Test case for delete user by id 
	@Test
	void testDeleteUserById_Success()
	{
		String id="Uid_01";
		when(userDao.deleteUserById(id)).thenReturn(Optional.of("user deleted.."));
		String deleteUserById = userServiceImpl.deleteUserById(id);
		System.out.println(deleteUserById);
	}

 	// 8. Scenario: The provided userId is null
	@Test
	void testDeletedUserById_UserNotFound()
	{
		String id="Uid_12";
		when(userDao.deleteUserById(id)).thenThrow(new UserNotFoundException("user not found with Id: "+id));
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.deleteUserById(id));
		assertEquals("user not found with Id: "+id, exception.getMessage());
		System.err.println(exception.getMessage());
	}

	
	// 9. Test case for testDeleteUserById_NullUserId()
	@Test
	void testDeleteUserById_NullUserId()
	{
		String id=null;
		when(userDao.deleteUserById(id)).thenThrow(new RuntimeException("User id must not be null"));
		Exception exception=assertThrows(RuntimeException.class,()->userServiceImpl.deleteUserById(id));
		assertEquals("User id must not be null", exception.getMessage());
		System.err.println(exception.getMessage());
	}
	

	// 10. Scenario: The provided userId is an empty string ("").
	@Test
	void testDeleteUserById_EmptyUserId()
	{
		String id="";
		Exception exception=assertThrows(RuntimeException.class,()->userServiceImpl.deleteUserById(id));
		assertEquals("Something went wrong", exception.getMessage());
		System.err.println(exception.getMessage());
	}

	
//================== TEST CASES-UPDATE USER BY ID ===============
	
	
	// 11. Test Case: Successful User Update
	@Test
	void testUpdateUserById_Success()
	{	
		when(userDao.updateByUserId(any(User.class),anyString())).thenReturn(Optional.of(user));
		UserResponseDTO response = userServiceImpl.updateUserById(updateUserRequestDTO, "Uid_01");
		assertEquals("venu@gmail.com", response.getUserEmail());
		assertEquals("venu", response.getUserName());
	}

	// 12. Test Case: updateUserRequestDTO is null
	@Test
	void testUpdateUserById_NullUpdateUserRequestDTO()
	{
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.updateUserById(null, "Uid_01"));
		assertEquals("UserRequestDTO must not be null", exception.getMessage());
		System.err.println(exception.getMessage());
	}

	// 13. Test Case: userId is null
	@Test
	void testUpdateUserById_NullUserId()
	{
		String id=null;
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.updateUserById(updateUserRequestDTO, id));
		assertEquals("Userid must not be null or blank", exception.getMessage());
		System.err.println(exception.getMessage());
	}

	// 14. Test Case: userId is Blank ("")
	@Test
	void testUpdateUserById_BlankUserId()
	{
		String id="";
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.updateUserById(updateUserRequestDTO, id));
		assertEquals("Userid must not be null or blank", exception.getMessage());
		System.err.println(exception.getMessage());
	}
	
	//15. Test Case: Error in DTO Conversion (DTOToEntity returns null) 
	@Test
	void testUpdateUserById_DTOConvertionFails()
	{
		try(MockedStatic<DTOToEntity> mockedConverter=mockStatic(DTOToEntity.class))
		{
			mockedConverter.when(()->DTOToEntity.userUpdateRequestDtoToUserEntity(any(UpdateUserRequestDTO.class))).thenReturn(null);
			
			Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.updateUserById(updateUserRequestDTO, "Uid_01"));
			assertEquals("User Not Updated , Try After Some Time", exception.getMessage());
			System.err.println(exception.getMessage());
		}
	}
	
	// 16. Test Case: userDao.updateByUserId() Returns Optional.empty()
	@Test 
	void testUpdateUserById_UpdateFails() 
	{
	    when(userDao.updateByUserId(any(User.class), anyString())).thenReturn(Optional.empty());

	    Exception exception = assertThrows(RuntimeException.class, () -> 
	        userServiceImpl.updateUserById(updateUserRequestDTO, "Uid_01")
	    );

	    assertEquals("User Not Updated , Try After Some Time", exception.getMessage());
	    System.err.println(exception.getMessage());
	}

	// 17. Test Case: EntityToDTO Conversion Fails (userResponseDTO is null)
	@Test 
	void testUpdateUserById_EntityToDTOConversionFails()
	{
		when(userDao.updateByUserId(any(User.class), anyString())).thenReturn(Optional.of(user));
		try(MockedStatic<EntityToDTO> mockConverter=mockStatic(EntityToDTO.class))
		{
			mockConverter.when(()->EntityToDTO.userEntityToUserResponseDTO(any(User.class))).thenReturn(null);
			Exception exception=assertThrows(RuntimeException.class,()-> userServiceImpl.updateUserById(updateUserRequestDTO, "Uid_01"));
			assertEquals("Something went wrong,try again some time", exception.getMessage());
		    System.err.println(exception.getMessage());
		}
	    
	}

//=============== TEST CASES- findUserById() ===============
	
	// 18. Test Case: Successful User Retrieval
	@Test
	void testFindUserById_Success()
	{
		String id="Uid_01";
		when(userDao.findByUserId(id)).thenReturn(Optional.of(user));
		UserResponseDTO userById = userServiceImpl.findUserById(id);
		assertEquals("venu", userById.getUserName());
		System.out.println(userById);
	}

	// 19. Test Case: userId is null
	@Test
	void testFindUserById_NullUserId() 
	{
	    Exception exception = assertThrows(RuntimeException.class, () ->userServiceImpl.findUserById(null));
	    assertEquals("Userid must not be null or blank", exception.getMessage());
	    System.err.println(exception.getMessage());
	}


	// 20. Test Case: userId is Blank ("") 
	@Test
	void testFindUserById_BlankUserId()
	{
	    Exception exception = assertThrows(RuntimeException.class, () ->userServiceImpl.findUserById(""));
	    assertEquals("Userid must not be null or blank", exception.getMessage());
	    System.err.println(exception.getMessage());
	}


	// 21. Test Case: User Not Found
	@Test
	void testFindUserById_UserNotFound()
	{
		String id="Uid_201";
		when(userDao.findByUserId(id)).thenReturn(Optional.empty());
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.findUserById(id));
		assertEquals("User Not Found with Id: "+id, exception.getMessage());
		System.err.println(exception.getMessage());
	}
	
	
	// 22. Test Case: Entity to DTO Conversion Fails (userResponseDTO is null)
	@Test
	void testFindUserById_EntityToDTOConvertionFails()
	{
		when(userDao.findByUserId("Uid_01")).thenReturn(Optional.of(user));
		try(MockedStatic<EntityToDTO> mockConverterMockedStatic=mockStatic(EntityToDTO.class))
		{
			mockConverterMockedStatic.when(()->EntityToDTO.userEntityToUserResponseDTO(any(User.class))).thenReturn(null);
			
			Exception exception = assertThrows(RuntimeException.class, ()-> userServiceImpl.findUserById("Uid_01"));
			assertEquals("Something went wrong, Try again sometime", exception.getMessage());
			System.err.println(exception.getMessage());
		}
		
	}

//================= Test Cases - LoginUserByEmailAndPassword() method ==================


	// 23. Test Case: Successful Login
	@Test
	void testLoginUserByEmailAndPassword_Success()
	{
		when(userDao.loginByEmailAndPassword("venu@gmail.com", "venu1234")).thenReturn(Optional.of(user));
		UserResponseDTO responseDTO = userServiceImpl.loginUserByEmailAndPassword("venu@gmail.com", "venu1234");
		assertEquals(7993494058l, responseDTO.getUserPhone());
		System.out.println(responseDTO);
		
	}

	// 24. Test Case: email is null
	@Test
	void testLoginUserByEmailAndPassword_NullEmail()
	{
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.loginUserByEmailAndPassword(null, "venu1234"));
		assertEquals("Email and Password must not be null or blank", exception.getMessage());
		System.err.println(exception.getMessage());
	}

	// 25. Test Case: password is null
	@Test
	void testLoginUserByEmailAndPassword_NullPassword()
	{
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.loginUserByEmailAndPassword("venu1234", null));
		assertEquals("Email and Password must not be null or blank", exception.getMessage());
		System.err.println(exception.getMessage());
	}

	// 26. Test Case: email is Blank ("")
	@Test
	void testLoginUserByEmailAndPassword_BlankEmail() 
	{
		Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.loginUserByEmailAndPassword("", "password123"));

       assertEquals("Email and Password must not be null or blank", exception.getMessage());
       System.err.println(exception.getMessage());
	}
	
	// 27. Test Case: password is Blank ("")
	@Test
	void testLoginUserByEmailAndPassword_BlankPassword() 
	{
		Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.loginUserByEmailAndPassword("test@gmail.com", ""));

        assertEquals("Email and Password must not be null or blank", exception.getMessage());
        System.err.println(exception.getMessage());
	}

	// 28. Test Case: Invalid Email Format
	@Test
	void testLoginUserByEmailAndPassword_InvalidEmailFormat() 
	{
	    Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.loginUserByEmailAndPassword("venuemail.com", "password123"));

	    assertEquals("Invalid Email Format", exception.getMessage());
	    System.err.println(exception.getMessage());
	}

	// 29. Test Case: Password Length is Less than 6
	@Test
	void testLoginUserByEmailAndPassword_ShortPassword() {
	    Exception exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.loginUserByEmailAndPassword("venu@gmail.com", "venu"));

	    assertEquals("Password Must be at least 6 characters", exception.getMessage());
	    System.err.println(exception.getMessage());
	}
	
	// 30. Test Case: User Not Found
	@Test
	void testLoginUserByEmailAndPassword_UserNotFound() 
	{
		when(userDao.loginByEmailAndPassword("hjjkkj@gmail.com", "venubibs")).thenReturn(Optional.empty());
		Exception e=assertThrows(RuntimeException.class, ()->userServiceImpl.loginUserByEmailAndPassword("hjjkkj@gmail.com", "venubibs"));
		assertEquals("User is not active", e.getMessage());
		System.err.println(e.getMessage());
	}

	
//================= Test cases for editUserImage() ===================
	
/*	
	// 31. Test Case: Successful Image Update (Not working properly)
	@Test
	void testEditUserImage_Success()
	{
	
		when(validImage.getContentType()).thenReturn("image/jpeg");
		when(validImage.getOriginalFilename()).thenReturn("profile.jpg");
		
		UserResponseDTO responseDTO = userServiceImpl.editUserImage(validImage, "Uid_01");
		assertNotNull(responseDTO);
		assertEquals("venu", responseDTO.getUserName());
	}
*/
	
	// 32. Test Case: userId is null
	@Test
	void testEditUserImage_NUllUserId()
	{
		Exception exception=assertThrows(RuntimeException.class, ()->userServiceImpl.editUserImage(validImage, null));
		assertEquals("Userid Must not be null or blank", exception.getMessage());
		System.err.println(exception.getMessage());
	}

	// 33. Test Case: userId is Blank ("")
	@Test
	void testEditUserImage_BlankUserId()
	{
		Exception exception = assertThrows(RuntimeException.class, () -> userServiceImpl.editUserImage(validImage, ""));

        assertEquals("Userid Must not be null or blank", exception.getMessage());
        System.err.println(exception.getMessage());

	}
	
	// 34. Test Case: file is null
	@Test
	void testEditUserImage_NullFile() {
	    Exception exception = assertThrows(RuntimeException.class, () -> 
	        userServiceImpl.editUserImage(null, "U123"));

	    assertEquals("File must not be null", exception.getMessage());
	    System.err.println(exception.getMessage());
	}

/*	
	// 35. Test Case: File Size Exceeds Limit  (Not working properly)
	@Test
	void testEditUserImage_FileSizeExceedsLimit() {
	    when(validImage.getSize()).thenReturn(5_000_000L); // 5MB file
	    when(fileStorageProperties.getMaxFileSize()).thenReturn(1_000_000L); // 1MB limit

	    Exception exception = assertThrows(RuntimeException.class, () -> 
	        userServiceImpl.editUserImage(validImage, "U123")
	    );

	    assertEquals("File size exceeds maximum limit! Supported file size 1000000", exception.getMessage());
	}
*/
/*
	// 36. (Not working properly)
	@Test
	void testEditUserImage_UserNotFound() {
	    
	    //when(userDao.editImage(validImage, "U123")).thenReturn(Optional.empty());

	    Exception exception = assertThrows(RuntimeException.class, () -> userServiceImpl.editUserImage(validImage, "U123"));

	    assertEquals("User does not exist with ID: " + "U123", exception.getMessage());
	}
*/	
	
	
}
