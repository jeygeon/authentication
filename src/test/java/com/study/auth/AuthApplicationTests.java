package com.study.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
class AuthApplicationTests {

    @Test
    void contextLoads() {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println(secretKey);
    }

}
