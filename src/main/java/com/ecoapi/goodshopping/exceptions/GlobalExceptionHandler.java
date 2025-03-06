package com.ecoapi.goodshopping.exceptions;

import com.ecoapi.goodshopping.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Allows the class to handle exceptions thrown by any controller in the app
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // The exception is thrown by Spring Security when a user tries to access a resource they don't have permission to access
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String message = "You do not have permission to access this resource ";
        ErrorResponse errorResponse = new ErrorResponse(message, request.getRequestURI());
        logger.warn("Access denied for request: {} - {}", request.getRequestURI(), ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
