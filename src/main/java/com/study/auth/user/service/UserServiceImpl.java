package com.study.auth.user.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.study.auth.exception.ErrorCode;
import com.study.auth.exception.exceptionDTO.ExistUserException;
import com.study.auth.exception.exceptionDTO.InvalidParameterException;
import com.study.auth.exception.exceptionDTO.UserNotFoundException;
import com.study.auth.user.dto.UserDTO;
import com.study.auth.user.entity.User;
import com.study.auth.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO saveUser(UserDTO userDTO) {

        // Exist User Check
        Optional<User> existUser = userRepository.findById(userDTO.getId());
        if (existUser.isPresent()) {
            throw new ExistUserException(HttpStatus.CONFLICT, ErrorCode.EXIST_USER, "User already exist.");
        }

        User saveUser = userRepository.save(userDTO.toEntity());
        return saveUser.toDTO();
    }

    @Override
    public UserDTO checkUser(UserDTO userDTO) {

        if (userDTO.getId() == null || userDTO.getPassword() == null) {
            throw new InvalidParameterException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PARAMETER, "Id or Password is not include.");
        }

        User user = userRepository.findUserByIdAndPassword(userDTO.getId(), userDTO.getPassword());
        if (user == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND, "User not found.");
        }

        return user.toDTO();
    }
}