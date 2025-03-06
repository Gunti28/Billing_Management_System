package com.srnrit.BMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		UserResponseDTO responseDTO = userService.saveUser(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
	}
	
	@DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String userId)
    {
    	System.out.println(userId);
    	return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }
	
}
