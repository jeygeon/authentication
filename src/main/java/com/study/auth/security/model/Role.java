package com.study.auth.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN, ROLE_USER");

    private String value;
}
