package com.gifthub.user.entity;


import com.gifthub.user.dto.KakaoUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity @Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("kakao")
public class KakaoUser extends User {

    private String kakaoAccountId;

    public KakaoUserDto toKakaoUserDto() {
      return KakaoUserDto.builder()
                .kakaoAccountId(this.kakaoAccountId)
                .date(super.getBirthDate())
                .email(super.getEmail())
                .name(super.getName())
                .nickname(super.getNickname())
                .gender(super.getGender())
                .year(super.getYear())
                .date(super.getBirthDate())
                .phoneNumber(super.getTel())
                .point(super.getPoint())
                .build();
    }



}
