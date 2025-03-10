package com.srnrit.BMS.globalexcepiton;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.srnrit.BMS.dto.Message;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler 
{
	@ExceptionHandler(exception = CategoryNotCreatedException.class)
    public ResponseEntity<?> categoryNotCreatedException(CategoryNotCreatedException e)
    {
   	 return new ResponseEntity<Message>( new  Message(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
	    String errorMessage = ex.getMessage();  
	    return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);  // Return only the message
	}
	
	  @ExceptionHandler(exception = MethodArgumentNotValidException.class) public
	  ResponseEntity<Map<String, String>>
	  handleValidationExceptions(MethodArgumentNotValidException ex) { Map<String,
	  String> errors = new HashMap<>();
	  
	  for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	  errors.put(error.getField(), error.getDefaultMessage()); } return new
	  ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST); 
	  }
	 
	 
	
}
