package com.study.auth.exception.dto;

import org.springframework.http.HttpStatus;

import com.study.auth.exception.ErrorCode;

import lombok.Getter;

@Getter
public class ExistUserException extends RuntimeException {

    private final HttpStatus status;

    private final ErrorCode errorCode;

    private final String detail;

    public ExistUserException(HttpStatus status, ErrorCode errorCode, String detail) {
        this.status = status;
        this.errorCode = errorCode;
        this.detail = detail;
    }
}
