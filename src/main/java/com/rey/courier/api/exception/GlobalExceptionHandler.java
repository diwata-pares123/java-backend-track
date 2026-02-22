package com.rey.courier.api.exception;

import jakarta.servlet.http.HttpServletRequest; // 🔥 New Import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime; // 🔥 New Import
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // 1. Changed return type to ApiErrorResponse
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) { // 2. Added request to get the path dynamically
        
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        // 3. Populate your new standardized enterprise contract!
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), // Extracts the integer 400
                "Validation Failed",
                errors, // Passing the Map into the 'Object message' field
                request.getRequestURI() // Dynamically gets the URL (e.g., "/api/v1/packages")
        );
        
        // 4. Return the new standard object
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}