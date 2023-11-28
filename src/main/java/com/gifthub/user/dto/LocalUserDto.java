package com.gifthub.user.dto;

import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.enumeration.UserType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalUserDto {

    private Long id;
    @NotNull
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\\\d!@#$%^&*]{8,}$")
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String nickname;
    @NotNull
    private String tel;
    @NotNull
    private String gender;
    @NotNull
    private String year;
    @NotNull
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
