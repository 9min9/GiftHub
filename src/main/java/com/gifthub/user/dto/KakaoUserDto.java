package com.gifthub.user.dto;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.enumeration.UserType;
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
    private Long id;
    private String  kakaoAccountId;
    private String email;
    private String name;
    private String nickname;
    private String tel;
    private String gender;
    private LocalDate year;
    private LocalDate birthDate;
    private Long point;

    public KakaoUser toEntity() {
        return KakaoUser.builder()
                .id(id)
                .kakaoAccountId(kakaoAccountId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .tel(tel)
                .gender(gender)
                .year(year)
                .birthDate(birthDate)
                .userType(UserType.USER)
                .point(point)
                .build();
    }




}
