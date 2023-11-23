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
private String gender;
private LocalDate birthyear;
private LocalDate birthdate;


public NaverUser toNaverEntity(){
    return (NaverUser) NaverUser.builder()
            .naver_id(naver_id)
            .email(email)
            .gender(gender)
            .year(birthyear)
            .birthDate(birthdate)
            .build();
}
}
