package com.study.auth.token;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.study.auth.token.dto.JwtTokenDTO;
import com.study.auth.token.dto.UserRefreshTokenDTO;
import com.study.auth.token.entity.UserRefreshToken;
import com.study.auth.token.repository.UserRefreshTokenRepository;
import com.study.auth.user.dto.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    // access token valid time (3 hours)
    @Value("${jwt.accessToken.time}")
    private long accessValidTime;

    // refresh token valid time (1 day)
    @Value("${jwt.refreshToken.time}")
    private long refreshValidTime;

    // secret key
    @Value("${jwt.secretKey}")
    private String secretKey;

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    public JwtTokenDTO generateToken(UserDTO userDTO) {

        // header generate (Map)
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("typ", "JWT");

        // payLoad generate (Map)
        Map<String, Object> payloads = new HashMap<String, Object>(); 
        payloads.put("Id", userDTO.getId()); 

        // secretKey generate
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // token valid time setting ( currentTime + validTime )
        Date currentDate = new Date();
        Date accessTokenExpireDate = new Date();
        Date refreshTokenExpireDate = new Date();
        accessTokenExpireDate.setTime(currentDate.getTime() + accessValidTime);
        refreshTokenExpireDate.setTime(currentDate.getTime() + refreshValidTime);

        // accessToken generate
        String accessJwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject(userDTO.getId())
                .setIssuedAt(currentDate)
                .setExpiration(accessTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refreshToken generate
        String refreshJwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject(userDTO.getId())
                .setIssuedAt(currentDate)
                .setExpiration(refreshTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refreshToken DB insert
        UserRefreshTokenDTO userRefreshTokenDTO = UserRefreshTokenDTO.builder()
                .id(userDTO.getId())
                .refreshToken(refreshJwt)
                .build();
        userRefreshTokenRepository.save(userRefreshTokenDTO.toEntity());

        JwtTokenDTO tokenDTO = JwtTokenDTO.builder()
                .accessToken(accessJwt)
                .build();

        return tokenDTO;
    }

    public String validationAccessToken(String accessToken, UserDTO userDTO) {

        if (isExpired(accessToken)) {
            Optional<UserRefreshToken> optionalUserRefreshToken = userRefreshTokenRepository.findById(userDTO.getId());
            UserRefreshTokenDTO usereRefreshTokenDTO = optionalUserRefreshToken.get().toDTO();
            String refreshToken = usereRefreshTokenDTO.getRefreshToken();
            return refreshToken;
        }
        return null;
    }

    public boolean validationRefreshToken(String refreshToken, UserDTO userDTO) {

        // refresh token 까지 만료 시 DB에서 delete후 logout처리
        if (isExpired(refreshToken)) {
            if (isExpired(refreshToken)) {
                userRefreshTokenRepository.deleteById(userDTO.getId());
                return true;
            }
        }
        return false;
    }

    public boolean isExpired(String token) {

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    public String reGenerateToken(UserDTO userDTO) {

        // header generate (Map)
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("typ", "JWT");

        // payLoad generate (Map)
        Map<String, Object> payloads = new HashMap<String, Object>(); 
        payloads.put("Id", userDTO.getId()); 

        // secretKey generate
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // token valid time setting ( currentTime + validTime )
        Date currentDate = new Date();
        Date accessTokenExpireDate = new Date();
        Date refreshTokenExpireDate = new Date();
        accessTokenExpireDate.setTime(currentDate.getTime() + accessValidTime);
        refreshTokenExpireDate.setTime(currentDate.getTime() + refreshValidTime);

        // accessToken generate
        String accessJwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject(userDTO.getId())
                .setIssuedAt(currentDate)
                .setExpiration(accessTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        JwtTokenDTO tokenDTO = JwtTokenDTO.builder()
                .accessToken(accessJwt)
                .build();

        return tokenDTO.getAccessToken();
    }
}
