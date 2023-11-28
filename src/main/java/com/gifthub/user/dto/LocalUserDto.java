package com.gifthub.user.dto;

import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.enumeration.UserType;
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
    private String name;
    private String nickname;
    private String tel;
    private String gender;
    private String year;
    private String birthdate;
    private Long point;

    public LocalUser toLocalEntity(){
        return LocalUser.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .tel(tel)
                .gender(gender)
                .year(year)
                .birthDate(birthdate)
                .userType(UserType.USER)
                .point(point)
                .build();
    }



}
