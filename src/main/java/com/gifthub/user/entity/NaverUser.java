package com.gifthub.user.entity;


import com.gifthub.user.dto.NaverUserDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("naver")
public class NaverUser extends User {

    private String naverId;

    public NaverUserDto toNaverUserDto() {
        return NaverUserDto.builder()
                .id(super.getId())
                .NaverId(this.naverId)
                .email(super.getEmail())
                .name(super.getName())
                .tel(super.getTel())
                .gender(super.getGender())
                .birthyear(super.getYear())
                .birthday(super.getBirthDate())
                .build();
    }
}
