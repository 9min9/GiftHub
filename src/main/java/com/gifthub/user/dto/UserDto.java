package com.gifthub.user.dto;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.NaverUser;
import com.gifthub.user.entity.User;
import com.gifthub.user.entity.enumeration.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.gifthub.user.entity.enumeration.UserType.USER;

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
    private String naverId;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .nickname(this.nickname)
                .gender(this.gender)
                .userType(USER)
                .birthDate(this.date)
                .year(this.year)
                .tel(this.tel)
                .point(this.point)
                .userType(USER)
                .build();
    }

    public LocalUser toLocalUserEntity() {
        return LocalUser.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .tel(tel)
                .year(year)
                .birthDate(date)
                .point(point)
                .userType(USER)
                .password(password).build();
    }

    public KakaoUser toKakaoUserEntity() {
        return KakaoUser.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .tel(tel)
                .year(year)
                .birthDate(date)
                .point(point)
                .userType(USER)
                .kakaoAccountId(kakaoAccountId).build();
    }

    public NaverUser toNaverUserEntity() {
        return NaverUser.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .gender(gender)
                .tel(tel)
                .year(year)
                .birthDate(date)
                .point(point)
                .userType(USER)
                .naverId(naverId).build();
    }

}
