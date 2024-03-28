package com.study.auth.user.service;

import com.study.auth.user.dto.UserDTO;

public interface UserService {

    /**
     * 사용자 검증
     * 
     * @param userDTO
     * @return userDTO
     */
    UserDTO checkUser(UserDTO userDTO);

    /**
     * 회원가입
     * 
     * @param userDTO
     * @return userDTO
     */
    UserDTO saveUser(UserDTO userDTO);
}
