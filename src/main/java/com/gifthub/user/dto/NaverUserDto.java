package com.gifthub.user.dto;


import com.gifthub.user.entity.NaverUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserDto {
private String naver_id;
private String email;
private String name;
private String nickname;
private String phonenumber;
private String gender;
private String birthyear;
private String birthday;


public NaverUser toNaverEntity(){
    return (NaverUser) NaverUser.builder()
            .naver_id(naver_id)
            .email(email)
            .name(name)
            .nickname(nickname)
            .phonenumber(phonenumber)
            .gender(gender)
            .year(birthyear)
            .birthDate(birthday)
            .build();
}
}
