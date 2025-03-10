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
import com.srnrit.BMS.exception.categoryexceptions.CategoryAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle CategoryNotCreatedException
    @ExceptionHandler(CategoryNotCreatedException.class)
    public ResponseEntity<Message> handleCategoryNotCreatedException(CategoryNotCreatedException e) {
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle CategoryNotFoundException
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Message> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<Message> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e) {
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    // Handle Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}