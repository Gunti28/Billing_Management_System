package com.srnrit.BMS.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dto.ChangePasswordRequestDTO;
import com.srnrit.BMS.dto.EmailRequestDTO;
import com.srnrit.BMS.dto.LoginRequestDTO;
import com.srnrit.BMS.dto.UpdateUserRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.dto.VerifyOTPRequestDTO;
import com.srnrit.BMS.service.UserService;
import com.srnrit.BMS.util.Message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated // Enables validation for method parameters
@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "/create", 
			     consumes = { MediaType.APPLICATION_JSON_VALUE },
			     produces = { MediaType.APPLICATION_JSON_VALUE }
	            )
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO dto) 
	{
		UserResponseDTO responseDTO = this.userService.saveUser(dto);
		URI uri=URI.create("/user/"+responseDTO.getUserId());
		return ResponseEntity.created(uri).body(responseDTO);
	}

	@PutMapping(value="/Updateuser/{userId}", 
		        consumes = { MediaType.APPLICATION_JSON_VALUE },
		        produces = { MediaType.APPLICATION_JSON_VALUE }
	           ) 
	public ResponseEntity<UserResponseDTO> updateUser (@Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO,@PathVariable @NotBlank(message = "User ID can't be blank or null!") String userId)
	{
		UserResponseDTO userResponseDTO = this.userService.updateUserById(updateUserRequestDTO, userId);
		return new ResponseEntity<UserResponseDTO>(userResponseDTO,HttpStatus.OK);
	}

    @DeleteMapping(value = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> deleteUserById(@PathVariable  @NotBlank(message = "User ID can't be blank or null!") String userId) 
	{
		String msg = this.userService.deleteUserById(userId);
		return ResponseEntity.status(HttpStatus.OK).body(new Message(msg));
			
		
	}

	@PutMapping(value = "/editProfileImage/{userid}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> editProfileImage
	                         (@RequestParam MultipartFile file,
	                          @PathVariable("userid") @NotBlank(message = "User ID can't be blank or null!") String userId) 
	{
		UserResponseDTO userResponseDTO = this.userService.editUserImage(file, userId);
		return new ResponseEntity<UserResponseDTO>(userResponseDTO, HttpStatus.OK);
	}

	@GetMapping(value = "/get/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getUserByUserId(@PathVariable @NotBlank(message = "User ID can't be blank or null!") String userId) 
	{
		UserResponseDTO responseDTO = this.userService.findUserById(userId);
		return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
	}

	@PostMapping(value = "/login", 
		         consumes = { MediaType.APPLICATION_JSON_VALUE },
		         produces = { MediaType.APPLICATION_JSON_VALUE }
	            )
	public ResponseEntity<?> userLoginByEmailAndPassword(@Valid @RequestBody LoginRequestDTO dto) 
	{
		UserResponseDTO responseDTO = this.userService.loginUserByEmailAndPassword(dto.getEmail(), dto.getPassword());
		return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
	}
    
	@GetMapping(value = "/allUsers", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() 
	{
		List<UserResponseDTO> allUser = this.userService.getAllUsers();
		return new ResponseEntity<List<UserResponseDTO>>(allUser, HttpStatus.OK);
	}

	
	@PostMapping(value="/VerifyEmail", 
		         consumes = { MediaType.APPLICATION_JSON_VALUE },
		         produces = { MediaType.APPLICATION_JSON_VALUE }
	             )
	public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO) 
	{
	    Message verifyUserByEmail = this.userService.verifyUserByEmail(emailRequestDTO);
	    return new ResponseEntity<Message>(verifyUserByEmail,HttpStatus.OK);
	}
	
	@PostMapping(value="/VerifyOTP", 
                  consumes = { MediaType.APPLICATION_JSON_VALUE},
		          produces = { MediaType.APPLICATION_JSON_VALUE }
	            )
	public ResponseEntity<?> verifyOTP (@Valid @RequestBody VerifyOTPRequestDTO verifyOTPRequestDTO) 
	{
	    Message verifyOTP = this.userService.verifyOTP(verifyOTPRequestDTO);	
		return new ResponseEntity<Message>(verifyOTP,HttpStatus.OK);
	}

	@PostMapping(value="/UpdatePassword", 
		         consumes = { MediaType.APPLICATION_JSON_VALUE},
		         produces = { MediaType.APPLICATION_JSON_VALUE }
	            )
	public ResponseEntity<UserResponseDTO> updatePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) 
	{
		UserResponseDTO updatePassword = this.userService.updatePassword(changePasswordRequestDTO);
		return new ResponseEntity<UserResponseDTO>(updatePassword,HttpStatus.OK);
	}

}
