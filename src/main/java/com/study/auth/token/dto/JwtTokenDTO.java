package com.study.auth.token.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtTokenDTO {

    private String accessToken;
}
