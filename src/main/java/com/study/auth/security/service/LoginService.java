package com.study.auth.security.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.study.auth.security.model.SUserRepository;
import com.study.auth.security.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService{

    private final SUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isPresent()) {

            User existUser = user.get();
            User authUser = User.builder()
                    .userName(existUser.getUsername())
                    .userId(existUser.getUserId())
                    .userPw(existUser.getUserPw())
                    .userRole(existUser.getUserRole())
                    .build();

            return authUser;
        }
        return null;
    }

}
