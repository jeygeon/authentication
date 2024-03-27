package com.study.auth.user.dto;

import com.study.auth.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {

    private String id;

    private String password;

    public User toEntity() {

        return User.builder()
                .id(this.id)
                .password(this.password)
                .build();
    }
}
