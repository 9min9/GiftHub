package com.gifthub.user.dto;

import com.gifthub.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String userType;
    private LocalDate birthDate;
    private String tel;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .userType(this.userType)
                .birthDate(this.birthDate)
                .tel(this.tel)
                .build();
    }

}
