package com.gifthub.user.dto;

import com.gifthub.user.entity.NaverUser;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.entity.enumeration.UserType;
import com.gifthub.user.service.UserAccountService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserDto {
    private Long id;
    private String NaverId;
    private String email;
    private String name;
    private String nickname;
    private String tel;
    private String gender;
    private String birthyear;
    private String birthday;
    private Long point;

    public NaverUser toNaverEntity() {
        return NaverUser.builder()
                .naverId(NaverId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .tel(UserAccountService.normalizePhoneNumber(tel))
                .gender(UserAccountService.normalizeGender(gender))
                .year(birthyear)
                .birthDate(UserAccountService.normalizeBirthDate(birthday))
                .userType(UserType.USER)
                .point(point)
                .build();
    }

    public UserDto toUserDto() {
        return UserDto.builder()
                .id(id)
                .email(email)
                .naverId(NaverId)
                .loginType(LoginType.NAVER.name())
                .userType(UserType.USER.getRole()).build();
    }
}
