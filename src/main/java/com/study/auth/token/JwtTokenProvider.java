package com.study.auth.token;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.study.auth.token.dto.JwtTokenDTO;
import com.study.auth.user.dto.UserDTO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    public JwtTokenDTO createToken(UserDTO userDTO) {

        /*
         * header 생성 > Map의 형태로 생성
         * payload 생성 > Map의 형태로 생성
         * signature 생성
         * 
         * token 생성
         */
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("typ", "JWT");

        Map<String, Object> payloads = new HashMap<String, Object>(); 
        payloads.put("KEY", "HelloWorld");
        payloads.put("NickName","Erjuer"); 
        payloads.put("Age","29");
        payloads.put("TempAuth","Y");

        // String str = "MyNickNameisErjuerAndNameisMinsu" 값을 byte 형변환 
        SecretKey key = Keys.hmacShaKeyFor("MyNickNameisErjuerAndNameisMinsu".getBytes(StandardCharsets.UTF_8));

        // 토큰 만료 시간
        Long expiredTime = 1000 * 60L * 60L * 1L;
        // 토큰 유효 시간 (밀리 세컨드 단위)
        Date expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + expiredTime);

        // 토큰 생성
        String jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject("Test")
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        JwtTokenDTO tokenDTO = JwtTokenDTO.builder()
                .accessToken(jwt)
                .refreshToken(jwt)
                .build();

        return tokenDTO;
    }

    public boolean checkExpiredToken() {
        
        return false;
    }

    public String createTokenByRefreshToken() {
        
        return "";
    }
}
