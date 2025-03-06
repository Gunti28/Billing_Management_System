package com.srnrit.BMS.exception.userexceptions;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException{
	
	public UserNotFoundException(String message) {
		super(message);
	}

}
