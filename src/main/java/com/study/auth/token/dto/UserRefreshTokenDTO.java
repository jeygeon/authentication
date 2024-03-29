package com.study.auth.token.dto;

import com.study.auth.token.entity.UserRefreshToken;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRefreshTokenDTO {

    private String id;

    private String refreshToken;

    public UserRefreshToken toEntity() {

        return UserRefreshToken.builder()
                .id(this.id)
                .refreshToken(this.refreshToken)
                .build();
    }
}
