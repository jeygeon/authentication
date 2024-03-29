package com.study.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.study.auth.token.JwtTokenProvider;
import com.study.auth.token.dto.JwtTokenDTO;
import com.study.auth.user.dto.UserDTO;
import com.study.auth.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController("/api")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    /**
     * 회원가입
     * 
     * @param userDTO
     * @return ResponseEntity<ResponseDTO>
     */
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody UserDTO userDTO) {

        // 사용자 생성
        userDTO = userService.saveUser(userDTO);
        return ResponseEntity.ok(ResponseDTO.response(userDTO, "user save success."));
    }

    /**
     * 로그인
     * 
     * @param request
     * @param userDTO
     * @return ResponseEntity<ResponseDTO>
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(HttpServletRequest request, @RequestBody UserDTO userDTO) {

        // 아이디, 패스워드 검증
        userDTO = userService.checkUser(userDTO);

        // access token, refresh token 생성
        JwtTokenDTO token = jwtTokenProvider.generateToken(userDTO);
        return ResponseEntity.ok(ResponseDTO.response(token, "token generate success."));
    }

    /**
     * 데이터 접근
     * 
     * @param request
     * @param userDTO
     * @return ResponseEntity<ResponseDTO>
     */
    @PostMapping("/access")
    public ResponseEntity<ResponseDTO> access(@RequestHeader("X-AUTH-ACCESS") String accessToken, @RequestBody UserDTO userDTO) {

        /*
         * access token 만료 체크
         * access token이 만료되었을 시 refresh token이 반환된다.
         */
        String refreshToken = jwtTokenProvider.validationAccessToken(accessToken, userDTO);
        if (refreshToken == null) {
            return ResponseEntity.ok(ResponseDTO.response(accessToken, "token pass."));
        }

        // refresh token 만료 체크
        boolean isExpired = jwtTokenProvider.validationRefreshToken(refreshToken, userDTO);
        if (isExpired) {
            return ResponseEntity.ok(ResponseDTO.response(accessToken, "expired token. user logout success."));
        }

        // access token만 만료되고 refresh token은 살아있을 경우 access token 재 발급 후 return
        String newAccessToken = jwtTokenProvider.reGenerateToken(userDTO);
        return ResponseEntity.ok(ResponseDTO.response(newAccessToken, "exist access token is expired. new token return success."));
    }
}
