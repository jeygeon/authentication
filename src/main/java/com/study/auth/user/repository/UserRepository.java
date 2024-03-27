package com.study.auth.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.study.auth.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public User findUserByIdAndPassword(@Param("id") String id, @Param("password") String password);
}
