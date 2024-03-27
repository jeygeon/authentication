package com.study.auth.user.service;

import java.util.Map;

import com.study.auth.user.dto.UserDTO;

public interface UserService {

    // 사용자 검증 (id, password)
    Map<String, Object> checkUser(UserDTO userDTO);
}
