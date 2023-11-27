package com.gifthub.user.dto;


import com.gifthub.user.entity.NaverUser;
import com.gifthub.user.entity.enumeration.UserType;
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
                .tel(tel)
                .gender(gender)
                .year(birthyear)
                .birthDate(birthday)
                .userType(UserType.USER)
                .point(point)
                .build();
    }
}
