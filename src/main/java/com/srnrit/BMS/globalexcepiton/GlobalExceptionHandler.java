package com.srnrit.BMS.globalexcepiton;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.srnrit.BMS.exception.userexceptions.InvalideOTPException;
import com.srnrit.BMS.exception.userexceptions.UnSupportedFileTypeException;
import com.srnrit.BMS.exception.userexceptions.UserAleadyExistException;
import com.srnrit.BMS.exception.userexceptions.UserNotFoundException;
import com.srnrit.BMS.exception.userexceptions.UserNotcreatedException;
import com.srnrit.BMS.util.Message;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNameAlreadyExistsException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler 
{
	@ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) 
		{
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(errors);
	}
	
	// Handle 415 Unsupported Media Type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Message> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) 
    {
         return ResponseEntity
        		.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new Message("The content type is not supported: " + ex.getMessage()));
    }

	@ExceptionHandler(exception = UserNotcreatedException.class)
	public ResponseEntity<Message> userNotCreatedException(UserNotcreatedException e) 
	{
		return  buildErrorResponse(e);
	}

	// Handle PathVariable validation errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex)
    {
        Map<String, String> errorResponse = new HashMap<>();
        // Extract the first error message from the exception
        ex.getConstraintViolations().forEach(violation -> 
            errorResponse.put("message", violation.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
	
	
	@ExceptionHandler(exception = UserNotFoundException.class)
	public ResponseEntity<Message> userNotFoundException(UserNotFoundException e) 
	{
		return  ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new Message(e.getMessage()));
	}
	
	@ExceptionHandler({
		                  UserAleadyExistException.class,
		                  NoResourceFoundException.class,
		                  UnSupportedFileTypeException.class,
		                  IllegalArgumentException.class
		                  
		              })
	public ResponseEntity<?> handleBadRequestException(Exception e)
	{
		return  buildErrorResponse(e, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e) 
	{
        return ResponseEntity
        		.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new Message("File size exceeds the maximum limit 10MB!"));
    } 
	
	@ExceptionHandler(exception = InvalideOTPException.class)
	public ResponseEntity<Message> invalidOTPException(InvalideOTPException e)
	{
		return  buildErrorResponse(e);
	}
	
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Message> runtimeException(RuntimeException e)
	{
		return  buildErrorResponse(e);
	}

	@ExceptionHandler(exception = CategoryNotCreatedException.class)
	public ResponseEntity<?> categoryNotCreatedException(CategoryNotCreatedException e)
	{
		return  buildErrorResponse(e);
	}
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
		return  buildErrorResponse(e);  
	}
	@ExceptionHandler(CategoryNameAlreadyExistsException.class)
	public ResponseEntity<Message> handleCategoryAlreadyExistsException(CategoryNameAlreadyExistsException e) {
		return  buildErrorResponse(e, HttpStatus.BAD_REQUEST);
	}
	// 🔥 Common method to avoid duplication
    private ResponseEntity<Message> buildErrorResponse(Exception e, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(new Message(e.getMessage()));
    }
	// 🔥 Common method to avoid duplication
    private ResponseEntity<Message> buildErrorResponse(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Message(e.getMessage()));
    }

}
