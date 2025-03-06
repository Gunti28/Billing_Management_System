package com.srnrit.BMS.globalexcepiton;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.util.Message;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(exception = UserNotcreatedException.class)
	public ResponseEntity<Message> userNotCreatedException(UserNotcreatedException e) 
	{
		return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(exception = UserNotFoundException.class)
	public ResponseEntity<Message> userNotFoundException(UserNotFoundException e) 
	{
		return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(exception = NoResourceFoundException.class)
	public ResponseEntity<?> noResouceFoundException(NoResourceFoundException ex)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = RuntimeException.class)
	public ResponseEntity<Message> runtimeException(RuntimeException e)
	{
		return new ResponseEntity<Message>(new Message(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<Message> exception(Exception e)
	{
		return new ResponseEntity<Message>(new Message(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
}
