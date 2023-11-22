package com.gifthub.user.entity;


import com.gifthub.user.dto.KakaoUserDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("KAKAO")
public class KakaoUser extends User {

    private String kakaoAccountId;

    public KakaoUserDto toKakaoUserDto() {
        return KakaoUserDto.builder()
                .kakaoAccountId(this.kakaoAccountId)
                .id(super.getId())
                .email(super.getEmail())
                .name(super.getName())
                .nickname(super.getNickname())
                .gender(super.getGender())
                .year(super.getYear())
                .birthDate(super.getBirthDate())
                .tel(super.getTel())
                .point(super.getPoint())
                .build();
    }


}
