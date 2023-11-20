package com.gifthub.user.dto;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.enumeration.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserDto {

    private String  kakaoAccountId;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String gender;
    private LocalDate year;
    private LocalDate date;
    private Long point;



    public KakaoUser toEntity() {
        return (KakaoUser) KakaoUser.builder()
                .kakaoAccountId(kakaoAccountId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .tel(phoneNumber)
                .gender(gender)
                .year(year)
                .birthDate(date)
                .loginType(LoginType.KAKAO)
                .build();
    }




}
