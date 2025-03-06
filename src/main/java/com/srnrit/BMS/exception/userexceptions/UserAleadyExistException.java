package com.srnrit.BMS.exception.userexceptions;

@SuppressWarnings("serial")
public class UserAleadyExistException extends RuntimeException{
	
	public UserAleadyExistException(String message) {
		super(message);
	}

}
