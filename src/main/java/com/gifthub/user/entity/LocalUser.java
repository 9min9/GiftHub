package com.gifthub.user.entity;

import com.gifthub.user.dto.UserDto;
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
@DiscriminatorValue("LOCAL")
public class LocalUser extends User {

    private String password;

    public UserDto toDto() {
        return UserDto.builder()
                .id(super.getId())
                .email(super.getEmail())
                .name(super.getName())
                .userType(super.getUserType().name())
                .year(super.getYear())
                .date(super.getBirthDate())
                .tel(super.getTel())
                .point(super.getPoint())
                .build();
    }

}
