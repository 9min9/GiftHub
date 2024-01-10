package com.gifthub.user.dto;

import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.entity.enumeration.UserType;
import com.gifthub.user.service.UserAccountService;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalUserDto {

    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String nickname;
    private String tel;
    private String gender;
    private String year;
    private String birthdate;
    private Long point;
    private LoginType loginType;
    private UserType userType;

    public LocalUser toLocalUserEntity() {
        return LocalUser.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .tel(UserAccountService.normalizePhoneNumber(tel))
                .gender(UserAccountService.normalizeGender(gender))
                .year(year)
                .birthDate(UserAccountService.normalizeBirthDate(birthdate))
                .userType(userType)
                .point(point)
                .build();
    }

    public UserDto toUserDto() {
        return UserDto.builder()
                .id(id)
                .email(email)
                .loginType(LoginType.KAKAO.name())
                .userType(userType.getRole()).build();
    }

}
