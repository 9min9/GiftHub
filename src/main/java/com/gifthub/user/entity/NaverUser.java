package com.gifthub.user.entity;


import com.gifthub.user.dto.NaverUserDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity @Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("naver")
public class NaverUser extends User{

 private String naver_id;
public NaverUserDto toNaverDto(){
return NaverUserDto.builder()
        .naver_id(this.naver_id)
        .email(super.getEmail())
        .name(super.getName())
        .gender(super.getGender())
        .birthdate(super.getYear())
        .birthdate(super.getBirthDate())
        .build();
}
}
