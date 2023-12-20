package com.gifthub.user.dto;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.entity.enumeration.UserType;
import com.gifthub.user.service.UserAccountService;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserDto {
    private Long id;
    private String kakaoAccountId;
    private String email;
    private String name;
    private String nickname;
    private String tel;
    private String gender;
    private String year;
    private String birthDate;
    private Long point;

    public KakaoUser toEntity() {
        return KakaoUser.builder()
                .id(id)
                .kakaoAccountId(kakaoAccountId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .tel(UserAccountService.normalizePhoneNumber(tel))
                .gender(UserAccountService.normalizeGender(gender))
                .year(year)
                .birthDate(UserAccountService.normalizeBirthDate(birthDate))
                .userType(UserType.USER)
                .point(point)
                .build();
    }

    public UserDto toUserDto() {
        return UserDto.builder()
                .id(id)
                .email(email)
                .kakaoAccountId(kakaoAccountId)
                .loginType(LoginType.KAKAO.name())
                .userType(UserType.USER.getRole()).build();
    }

}
