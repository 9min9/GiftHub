package com.gifthub.user.dto;

import com.gifthub.user.entity.User;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.entity.enumeration.UserType;
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
    private String password;
    private String name;
    private String email;
    private String userType;
    private String loginType;
    private LocalDate year;
    private LocalDate date;
    private String tel;
    private Long point;

    public User toEntity() {
        return (User) User.builder()
                .id(this.id)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .userType(UserType.ADMIN)
                .loginType(LoginType.GIFT_HUB)
                .birthDate(this.date)
                .tel(this.tel)
                .point(this.point)
                .build();
    }

}
