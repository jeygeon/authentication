package com.study.auth.token.entity;

import com.study.auth.token.dto.UserRefreshTokenDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Entity(name = "user_refresh_token")
public class UserRefreshToken {

    @Id
    private String id;

    private String refreshToken;

    public UserRefreshTokenDTO toDTO() {

        return UserRefreshTokenDTO.builder()
                .id(this.id)
                .refreshToken(this.refreshToken)
                .build();
    }
}
