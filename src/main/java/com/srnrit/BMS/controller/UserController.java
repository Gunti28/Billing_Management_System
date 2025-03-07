package com.srnrit.BMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.srnrit.BMS.dto.LoginRequestDTO;
import com.srnrit.BMS.dto.UserRequestDTO;
import com.srnrit.BMS.dto.UserResponseDTO;
import com.srnrit.BMS.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/create",
			     consumes = {"application/json","application/xml"},
			     produces = {"application/json"})
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO dto)
	{
		UserResponseDTO responseDTO = this.userService.saveUser(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}
	

	@DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable String userId)
    {
    	String msg = this.userService.deleteUserById(userId);
    	return ResponseEntity.status(HttpStatus.OK).body(msg);
    }
	
	
	@PutMapping(value="/editProfileImage/{userid}")
	public ResponseEntity<?> editProfileImage(@RequestParam MultipartFile file,
			                                  @PathVariable("userid") String userId
			)
	{
		System.out.println("UserController.editProfileImage()");
		System.out.println(userId);
		 UserResponseDTO userResponseDTO = this.userService.editUserImage(file,userId);
		 
		 return new ResponseEntity<UserResponseDTO>(userResponseDTO,HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/get/{userId}")
	public ResponseEntity<?> getUserByUserId(@PathVariable String userId)
	{
		UserResponseDTO responseDTO = this.userService.findUserById(userId);
		return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
	}
	
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> userLoginByEmailAndPassword(@Valid @RequestBody LoginRequestDTO dto)
	{
		UserResponseDTO responseDTO = this.userService.loginUserByEmailAndPassword(dto.getEmail(), dto.getPassword());
	    return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
	}
	
	
	
	
	

	
}
