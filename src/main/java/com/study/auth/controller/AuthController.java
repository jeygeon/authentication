package com.study.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.auth.token.JwtTokenProvider;
import com.study.auth.token.dto.JwtTokenDTO;
import com.study.auth.user.dto.UserDTO;
import com.study.auth.user.entity.User;
import com.study.auth.user.repository.UserRepository;
import com.study.auth.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final UserService userService;

    @GetMapping("/signUp")
    public void signUp() {

    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<?>> login(HttpServletRequest request, @RequestBody UserDTO userDTO) {

        // Id, Password validation
        User findUser = userRepository.findUserByIdAndPassword(userDTO.getId(), userDTO.getPassword());
        if (findUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.response("user not found"));
        }

        Map<String, Object> map = userService.checkUser(userDTO);
        if ((boolean) map.get("result")) {
            switch (String.valueOf(map.get("msg"))) {
            case "BAD_REQUEST" :
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.response("Id or Password is null"));
            case "NOT_FOUND_USER" :
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.response("user not found"));
            }
        }

        // accessToken, refreshToken generate
        JwtTokenDTO token = jwtTokenProvider.createToken((UserDTO) map.get("user"));
        return ResponseEntity.ok(ResponseDTO.response(token, "token generate success"));
    }
}
