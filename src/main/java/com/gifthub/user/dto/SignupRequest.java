package com.gifthub.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$")
    private String password;

    @NotBlank
    private String confirmPassword;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String name;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String nickname;
    @NotBlank
    @Pattern(regexp = "^010[0-9]{7,8}$")
    private String tel;
    private Boolean validateMobile;
    @NotBlank
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$")
    private String birthDate;
    @NotBlank
    private String gender;


    public LocalUserDto toLocalUserDto() {
        String year = birthDate.substring(0, 4);
        String birthDay = birthDate.substring(4);

        return LocalUserDto.builder()
                .email(email)
                .password(password)
                .confirmPassword(confirmPassword)
                .name(name)
                .nickname(nickname)
                .tel(tel)
                .gender(gender)
                .year(year)
                .birthdate(birthDay).build();

    }
}

