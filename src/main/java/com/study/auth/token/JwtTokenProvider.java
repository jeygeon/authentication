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
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public String generateToken(UserDTO userDTO) {

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
        log.info("[Id : " + userDTO.getId() + "] Access token generate success > " + accessJwt);

        // refreshToken generate
        String refreshJwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject(userDTO.getId())
                .setIssuedAt(currentDate)
                .setExpiration(refreshTokenExpireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        log.info("[Id : " + userDTO.getId() + "] Refresh token generate success > " + refreshJwt);

        // refreshToken DB insert
        UserRefreshTokenDTO userRefreshTokenDTO = UserRefreshTokenDTO.builder()
                .id(userDTO.getId())
                .refreshToken(refreshJwt)
                .build();
        userRefreshTokenRepository.save(userRefreshTokenDTO.toEntity());

        JwtTokenDTO tokenDTO = JwtTokenDTO.builder()
                .accessToken(accessJwt)
                .build();

        return tokenDTO.getAccessToken();
    }

    public boolean validationAccessToken(String accessToken, UserDTO userDTO) {

        if (isExpired(accessToken, userDTO)) {
            log.info("[Id : " + userDTO.getId() + "] Access token is Expired.");
            return false;
        }

        log.info("[Id : " + userDTO.getId() + "] Access token is available.");
        return true;
    }

    public boolean validationRefreshToken(UserDTO userDTO) {

        Optional<UserRefreshToken> optionalUserRefreshToken = userRefreshTokenRepository.findById(userDTO.getId());
        UserRefreshTokenDTO usereRefreshTokenDTO = optionalUserRefreshToken.get().toDTO();
        String refreshToken = usereRefreshTokenDTO.getRefreshToken();
        if (isExpired(refreshToken, userDTO)) {
            userRefreshTokenRepository.deleteById(userDTO.getId());
            log.info("[Id : " + userDTO.getId() + "] Refresh token is Expired. Remove token success.");
            return false;
        }
        log.info("[Id : " + userDTO.getId() + "] Refresh token is available.");
        return true;
    }

    public boolean isExpired(String token, UserDTO userDTO) {

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build();
        Date expiration;
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException e) {
            log.info("[Id : " + userDTO.getId() + "] Token is expired > " + token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("[Id : " + userDTO.getId() + "] Token is out of form > " + token);
            return true;
        } catch (IllegalArgumentException e) {
            log.info("[Id : " + userDTO.getId() + "] Token is invalid > " + token);
            return true;
        }
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
        log.info("[Id : " + userDTO.getId() + "] Access token reGenerate success > " + accessJwt);

        JwtTokenDTO tokenDTO = JwtTokenDTO.builder()
                .accessToken(accessJwt)
                .build();

        return tokenDTO.getAccessToken();
    }
}
