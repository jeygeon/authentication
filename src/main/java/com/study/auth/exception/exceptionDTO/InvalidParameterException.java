package com.study.auth.exception.exceptionDTO;

import org.springframework.http.HttpStatus;

import com.study.auth.exception.ErrorCode;

import lombok.Getter;

@Getter
public class InvalidParameterException extends RuntimeException {

    private final HttpStatus status;

    private final ErrorCode errorCode;

    private final String detail;

    public InvalidParameterException(HttpStatus status, ErrorCode errorCode, String detail) {
        this.status = status;
        this.errorCode = errorCode;
        this.detail = detail;
    }
}
