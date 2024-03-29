package com.study.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.study.auth.exception.exceptionDTO.ExistUserException;
import com.study.auth.exception.exceptionDTO.InvalidParameterException;
import com.study.auth.exception.exceptionDTO.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorDTO> userNotFoundExceptionHandler(UserNotFoundException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ErrorDTO> invalidParameterExceptionHandler(InvalidParameterException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }

    @ExceptionHandler(ExistUserException.class)
    protected ResponseEntity<ErrorDTO> existUserExceptionHandler(ExistUserException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }

}
