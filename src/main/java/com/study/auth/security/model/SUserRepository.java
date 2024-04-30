package com.study.auth.security.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SUserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUserId(String id);
}
