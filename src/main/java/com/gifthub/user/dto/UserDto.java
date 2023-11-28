package com.gifthub.user.dto;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.User;
import com.gifthub.user.entity.enumeration.UserType;
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
    private String nickname;
    private String userType;
    private String loginType;
    private String gender;
    private String year;
    private String date;
    private String tel;
    private Long point;
    private String kakaoAccountId;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .nickname(this.nickname)
                .gender(this.gender)
                .password(this.password)
                .userType(UserType.USER)
                .birthDate(this.date)
                .year(this.year)
                .tel(this.tel)
                .point(this.point)
                .build();
    }

    public LocalUser toLocalUserEntity() {
        return LocalUser.builder().password(this.password).build();
    }

    public KakaoUser toKakaoUserEntity() {
        return KakaoUser.builder().kakaoAccountId(this.kakaoAccountId).build();
    }

}
