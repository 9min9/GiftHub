package com.gifthub.user.dto;

import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.enumeration.UserType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.parameters.P;

import static com.gifthub.user.entity.enumeration.UserType.USER;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalUserDto {

    private Long id;

    @NotBlank
    @Pattern(regexp = "^(?=.*[@])*\\S*$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$")
    private String password;


    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$")
    private String confirmpassword;

    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\S*$" ,  message="닉네임: 공백 체크해주세요.")
    private String nickname;


    @NotBlank
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-([0-9]{3,4})-([0-9]{4})\\S*$")
    private String tel;

    @NotBlank
    private String gender;

    @NotBlank
    @Pattern(regexp = "^\\d{4}$")
    private String year;

    @NotNull
    private String birthdate;

    private Long point;

    private UserType userType = USER;

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
                .userType(userType)
                .point(point)
                .build();
    }

}
