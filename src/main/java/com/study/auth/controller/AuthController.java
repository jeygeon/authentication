package com.study.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.auth.token.JwtTokenProvider;
import com.study.auth.token.dto.JwtTokenDTO;
import com.study.auth.user.dto.UserDTO;
import com.study.auth.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {

        // Save user
        userDTO = userService.saveUser(userDTO);
        return ResponseEntity.ok(ResponseDTO.response(userDTO, "user save success"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody UserDTO userDTO) {

        // Id, Password validation
        userDTO = userService.checkUser(userDTO);

        // accessToken, refreshToken generate
        JwtTokenDTO token = jwtTokenProvider.generateToken(userDTO);
        return ResponseEntity.ok(ResponseDTO.response(token, "token generate success"));
    }
}
