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

    @NotBlank(message = "이메일: 입력해주세요.")
    @Pattern(regexp = "^(?=.*[@])*\\S*$" ,  message="이메일: 공백 및 형태 체크해주세요.")
    private String email;

    @NotBlank(message = "비밀번호: 입력해주세요.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$" ,message = "비밀번호:  8자리 이상 20이하, 특수문자를 포함해서 작성해주세요")
    private String password;


    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$" ,message = "비밀번호:  8자리 이상 20이하, 특수문자를 포함해서 작성해주세요")
    private String confirmpassword;

    @NotBlank(message = "이름: 입력해주세요.")
    @Pattern(regexp = "^\\S*$" ,  message="이름: 공백 체크해주세요.")
    private String name;

    @NotBlank(message = "닉네임: 입력해주세요.")
    @Pattern(regexp = "^\\S*$" ,  message="닉네임: 공백 체크해주세요.")
    private String nickname;


    @NotBlank(message = "전화번호: 입력해주세요.")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-([0-9]{3,4})-([0-9]{4})\\S*$", message = "전화번호: 공백 및 - 확인해주세요 ")
    private String tel;

    @NotBlank(message = "성별 : 체크해주세요")
    private String gender;

    @NotBlank(message = "생년월일: 입력해주세요.")
    @Pattern(regexp = "^\\d{4}$", message ="생년월일: 형태에 맞게 입력해주세요" )
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
