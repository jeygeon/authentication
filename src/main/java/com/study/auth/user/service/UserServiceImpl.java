package com.study.auth.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.study.auth.user.dto.UserDTO;
import com.study.auth.user.entity.User;
import com.study.auth.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Map<String, Object> checkUser(UserDTO userDTO) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", false);

        if (userDTO.getId() == null || userDTO.getPassword() == null) {
            resultMap.put("msg", "BAD_REQUEST");
            return resultMap;
        }

        User user = userRepository.findUserByIdAndPassword(userDTO.getId(), userDTO.getPassword());
        if (user == null) {
            resultMap.put("msg", "NOT_FOUND_USER");
            return resultMap;
        }

        resultMap.put("user", user.toDTO());
        resultMap.put("result", true);
        return resultMap;
    }
}
