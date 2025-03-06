package com.srnrit.BMS.globalexcepiton;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.util.Message;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(exception = UserNotcreatedException.class)
    public ResponseEntity<Message>  userNotCreatedException(UserNotcreatedException e)
    {
   	 return new ResponseEntity<Message>(new Message(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
	@ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<Message>  userNotFoundException(UserNotFoundException e)
    {
   	 return new ResponseEntity<Message>(new Message(e.getMessage()),HttpStatus.NOT_FOUND);
    }

}
