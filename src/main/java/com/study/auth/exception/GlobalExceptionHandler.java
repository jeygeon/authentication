package com.study.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.study.auth.exception.dto.ExistUserException;
import com.study.auth.exception.dto.InvalidParameterException;
import com.study.auth.exception.dto.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorDTO> userNotFoundExceptionHandler(UserNotFoundException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ErrorDTO> invalidParameterException(InvalidParameterException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }

    @ExceptionHandler(ExistUserException.class)
    protected ResponseEntity<ErrorDTO> existUserException(ExistUserException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }

}
