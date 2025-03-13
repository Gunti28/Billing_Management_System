package com.srnrit.BMS.globalexcepiton;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.srnrit.BMS.dto.Message;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNameAlreadyExistsException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handle Validation Errors and Return Only Messages
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new LinkedHashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> 
		errors.putIfAbsent(error.getField(), error.getDefaultMessage())
				);

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(exception = CategoryNotCreatedException.class)
	public ResponseEntity<?> categoryNotCreatedException(CategoryNotCreatedException e)
	{
		return new ResponseEntity<Message>( new  Message(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
		String errorMessage = ex.getMessage();  
		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);  
	}


	// Handle Validation Errors (Return single combined error message)

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(CategoryNameAlreadyExistsException.class)
	public ResponseEntity<Message> handleCategoryAlreadyExistsException(CategoryNameAlreadyExistsException e) {
		return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
}
