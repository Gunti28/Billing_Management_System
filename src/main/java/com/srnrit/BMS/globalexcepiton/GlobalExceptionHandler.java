package com.srnrit.BMS.globalexcepiton;

import java.util.stream.Collectors;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.srnrit.BMS.dto.Message;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotCreatedException;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.exception.productexceptions.ProductNotCreatedException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Validation Errors (Return single combined error message)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Message> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new Message(errorMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotCreatedException.class)
    public ResponseEntity<Message> productNotCreatedException(ProductNotCreatedException e) {
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Message> productNotFoundException(ProductNotFoundException e) {
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotCreatedException.class)
    public ResponseEntity<Message> categoryNotCreatedException(CategoryNotCreatedException e) {
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Message> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle IllegalArgumentException (e.g., duplicate products)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Message> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.CONFLICT);
    }

    // Handle Unknown Exceptions (Fallback for all unhandled cases)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(new Message("An unexpected error occurred: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<String> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    
    
}