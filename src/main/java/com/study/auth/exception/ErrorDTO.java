package com.study.auth.exception;

import org.springframework.http.ResponseEntity;

import com.study.auth.exception.dto.ExistUserException;
import com.study.auth.exception.dto.InvalidParameterException;
import com.study.auth.exception.dto.UserNotFoundException;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {

    private String code;

    private String detail;

    public static ResponseEntity<ErrorDTO> toResponseEntity(UserNotFoundException ex) {

        ErrorCode errorType = ex.getErrorCode();
        String detail = ex.getDetail();
 
        return ResponseEntity
            .status(ex.getStatus())
            .body(ErrorDTO.builder()
                .code(errorType.getStatus())
                .detail(detail)
                .build());
    }

    public static ResponseEntity<ErrorDTO> toResponseEntity(InvalidParameterException ex) {

        ErrorCode errorType = ex.getErrorCode();
        String detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDTO.builder()
                        .code(errorType.getStatus())
                        .detail(detail)
                        .build());
    }

    public static ResponseEntity<ErrorDTO> toResponseEntity(ExistUserException ex) {
        ErrorCode errorType = ex.getErrorCode();
        String detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDTO.builder()
                        .code(errorType.getStatus())
                        .detail(detail)
                        .build());
    }
}