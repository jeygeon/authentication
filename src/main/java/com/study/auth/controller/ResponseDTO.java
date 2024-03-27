package com.study.auth.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ResponseDTO<T> {

    private String msg;

    private T t;

    public static<T> ResponseDTO<T> response(T t, String msg) {

        return ResponseDTO.<T>builder()
                .msg(msg)
                .t(t)
                .build();
    }

    public static<T> ResponseDTO<T> response(String msg) {

        return ResponseDTO.<T>builder()
                .msg(msg)
                .build();
    }
}
