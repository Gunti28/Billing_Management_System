package com.srnrit.BMS.userdaotest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.srnrit.BMS.entity.User;
import com.srnrit.BMS.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
	
    //Create user  a method to generate a test user
    private User createTestUser(String email) {
        User user = new User();
        user.setUserName("Test User");
        user.setUserEmail(email);
        user.setUserPassword("password123");
        user.setDateOfRegistration(LocalDateTime.now());
        user.setActive(true);
        user.setUserPhone(1234567890L);
        user.setUserGender("Male");
        user.setTermsAndConditions(true);
        return user;
    }
    
    
    //1.User findByUserEmail(String userEmail);
    @Test
    void findByUserEmail_ExistingUser_ReturnsUser() {
        String testEmail = "test@example.com";
        User user = createTestUser(testEmail);
        userRepository.save(user);
        User foundUser = userRepository.findByUserEmail(testEmail);
        assertNotNull(foundUser);
        assertEquals(testEmail, foundUser.getUserEmail());
        assertEquals("Test User", foundUser.getUserName());
    }

    @Test
    void findByUserEmail_NonExistingEmail_ReturnsNull() {
        String nonExistentEmail = "nonexistent@example.com";
        User foundUser = userRepository.findByUserEmail(nonExistentEmail);
        assertNull(foundUser);
    }

    @Test
    void findByUserEmail_CaseSensitivity() {
        String originalEmail = "test@example.com";
        User user = createTestUser(originalEmail);
        userRepository.save(user);
        // Check exact case match
        User exactMatchUser = userRepository.findByUserEmail(originalEmail);
        assertNotNull(exactMatchUser);

        // Check different case
        User differentCaseUser = userRepository.findByUserEmail("TEST@EXAMPLE.COM");
        
        // This assertion might vary based on your database configuration
        // Some databases are case-sensitive, some are not
        if (differentCaseUser != null) {
            assertEquals(originalEmail.toLowerCase(), differentCaseUser.getUserEmail().toLowerCase());
        }
    }

    @Test
    void findByUserEmail_ExistingEmail_ReturnsCorrectUser() {
        String testEmail = "k.venukumar6543@gmail.com";
        
        // Create a comprehensive test user
        User user = new User();
        user.setUserName("Test User");
        user.setUserEmail(testEmail);
        user.setUserPassword("password123");
        user.setDateOfRegistration(LocalDateTime.now());
        user.setActive(true);
        user.setUserPhone(1234567890L);
        user.setUserGender("Male");
        user.setTermsAndConditions(true);

        // Save the user
        User savedUser = userRepository.save(user);
        User foundUser = userRepository.findByUserEmail(testEmail);
        assertNotNull(foundUser);
        assertEquals(savedUser.getUserId(), foundUser.getUserId());
        assertEquals(testEmail, foundUser.getUserEmail());
        assertEquals("Test User", foundUser.getUserName());
        assertTrue(foundUser.getActive());
    }

    @Test
    void findByUserEmail_EmptyEmail_HandledGracefully() 
    {
        User foundUser = userRepository.findByUserEmail("");
        assertNull(foundUser);
    }

    //2.User findByUserPassword(String userPassword);
    @Test
    void findByUserPassword_ExistingUser_ReturnsUser() 
    {
        String testPassword = "password123";
        User user = createTestUser(testPassword);
        userRepository.save(user);
        User foundUser = userRepository.findByUserPassword(testPassword);
        assertNotNull(foundUser);
        assertEquals(testPassword, foundUser.getUserPassword());
    }

    
    @Test
    void findByUserPassword_NonExistingPassword_ReturnsNull() {
        String nonExistentPassword = "nonexistentpassword";
        User foundUser = userRepository.findByUserPassword(nonExistentPassword);
        assertNull(foundUser);
    }

    
    //3.User findByUserPhone(Long userphonenumber);
    // Positive Test Case
    @Test
    void findByUserPhone_ExistingUser_ReturnsUser() {
        Long testPhoneNumber = 1234567890L;
        User user = createTestUser("test@example.com");
        user.setUserPhone(testPhoneNumber);
        userRepository.save(user);
        User foundUser = userRepository.findByUserPhone(testPhoneNumber);
        assertNotNull(foundUser);
        assertEquals(testPhoneNumber, foundUser.getUserPhone());
        assertEquals(user.getUserName(), foundUser.getUserName());
        assertEquals(user.getUserEmail(), foundUser.getUserEmail());
    }

    // Negative Test Case
    @Test
    void findByUserPhone_NonExistingPhoneNumber_ReturnsNull() {
        Long nonExistentPhoneNumber = 9876543210L;
        User foundUser = userRepository.findByUserPhone(nonExistentPhoneNumber);
        assertNull(foundUser);
    }
    
    
    //4.User findByUserEmailAndUserPassword(String userEmail,String userPassword);
    // Positive Test Case
    @Test
    void findByUserEmailAndUserPassword_ValidCredentials_ReturnsUser() 
    {
        String testEmail = "test@example.com";
        String testPassword = "password123";
        
        User user = createTestUser(testEmail);
        user.setUserPassword(testPassword);
        userRepository.save(user);
        User foundUser = userRepository.findByUserEmailAndUserPassword(testEmail, testPassword);
        assertNotNull(foundUser);
        assertEquals(testEmail, foundUser.getUserEmail());
        assertEquals(testPassword, foundUser.getUserPassword());
    }

    // Negative Test Case
    @Test
    void findByUserEmailAndUserPassword_InvalidCredentials_ReturnsNull() 
    {
        String validEmail = "valid@example.com";
        String validPassword = "correctpassword";
        String invalidPassword = "wrongpassword";
        // Create a user with valid credentials
        User user = createTestUser(validEmail);
        user.setUserPassword(validPassword);
        userRepository.save(user);
        // Try with incorrect password
        User foundUserWrongPassword = userRepository.findByUserEmailAndUserPassword(validEmail, invalidPassword); 
        // Try with non-existing email
        User foundUserWrongEmail = userRepository.findByUserEmailAndUserPassword("wrong@example.com", validPassword);
        assertNull(foundUserWrongPassword);
        assertNull(foundUserWrongEmail);
    }
    
    @Test
    void findByUserEmailAndUserPassword_InactiveUser() 
    {
        String testEmail = "inactive@example.com";
        String testPassword = "password123";     
        User inactiveUser = createTestUser(testEmail);
        inactiveUser.setUserPassword(testPassword);
        inactiveUser.setActive(false);
        userRepository.save(inactiveUser);
        User foundUser = userRepository.findByUserEmailAndUserPassword(testEmail, testPassword);
        assertNotNull(foundUser);
        
    }

    @Test
    void findByUserEmailAndUserPassword_EmptyInputs() 
    {
        User foundUserEmptyEmail = userRepository.findByUserEmailAndUserPassword("", "password");
        User foundUserEmptyPassword = userRepository.findByUserEmailAndUserPassword("test@example.com", "");
        User foundUserBothEmpty = userRepository.findByUserEmailAndUserPassword("", "");
        assertNull(foundUserEmptyEmail);
        assertNull(foundUserEmptyPassword);
        assertNull(foundUserBothEmpty);
    }

    
    //4.Optional<User> findByUserId(String userId);
    // Positive Test Case
    @Test
    void findByUserId_ExistingUser_ReturnsUser() 
    {
        User user = createTestUser("test@example.com");
        User savedUser = userRepository.save(user);
        String savedUserId = savedUser.getUserId();
        Optional<User> foundUser = userRepository.findByUserId(savedUserId);
        assertTrue(foundUser.isPresent(), "User should be found with existing user ID");
        assertEquals(savedUserId, foundUser.get().getUserId());
        assertEquals(user.getUserEmail(), foundUser.get().getUserEmail());
    }

    // Negative Test Case
    @Test
    void findByUserId_NonExistingUserId_ReturnsEmptyOptional() 
    {
        String nonExistentUserId = "NONEXISTENT_USER_ID";
        Optional<User> foundUser = userRepository.findByUserId(nonExistentUserId);
        assertTrue(foundUser.isEmpty(), "Optional should be empty for non-existing user ID");
    }
    
    @Test
    void findByUserId_NullUserId_ReturnsEmptyOptional() 
    {
        Optional<User> foundUser = userRepository.findByUserId(null);
        assertTrue(foundUser.isEmpty(), "Optional should be empty for null user ID");
    }

    @Test
    void findByUserId_EmptyUserId_ReturnsEmptyOptional() 
    {
        Optional<User> foundUser = userRepository.findByUserId("");
        assertTrue(foundUser.isEmpty(), "Optional should be empty for empty user ID");
    }
    
}