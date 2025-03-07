package com.srnrit.BMS.globalexcepiton;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@ExceptionHandler(exception = CategoryNotFoundException.class)
   public ResponseEntity<?> categoryNotFoundException(CategoryNotFoundException e)
   {
  	 return new ResponseEntity<Message>( new  Message(e.getMessage()),HttpStatus.NOT_FOUND);
   }
}
