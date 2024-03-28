package com.study.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EXIST_USER("001_EXIST_USER"),
    USER_NOT_FOUND("002_USER_NOT_FOUND"),
    INVALID_PARAMETER("003_INVALID_PARAMETER");

    private final String status;
}
