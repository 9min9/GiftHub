package com.gifthub.user.dto;

import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.enumeration.UserType;
import com.gifthub.user.service.UserAccountService;
import lombok.*;

import static com.gifthub.user.entity.enumeration.UserType.USER;

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
    private UserType userType = USER;       //todo : USER로 설정하는 부분을 변경해야 할듯

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

}
