package com.study.auth.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.auth.token.entity.UserRefreshToken;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, String> {

}
