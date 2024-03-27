package com.study.auth.user.entity;

import com.study.auth.user.dto.UserDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "user")
public class User {

    @Id
    private String id;

    private String password;

    public UserDTO toDTO() {

        return UserDTO.builder()
                .id(this.id)
                .password(this.password)
                .build();
    }
}
