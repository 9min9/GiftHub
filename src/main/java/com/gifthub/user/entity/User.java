package com.gifthub.user.entity;

import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    private Long id;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private String name;
    private String userType;
    private LocalDate birthDate;
    private String tel;
    private Long point;

    public Long  usePoint(Long price){
        this.point-= price;

        return this.point;
    }

    public  Long plusPoint(Long price){
        this.point += price;

        return this.point;
    }

    public UserDto toDto() {
        return UserDto.builder()
                .id(this.id)
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .userType(this.userType)
                .birthDate(this.birthDate)
                .tel(this.tel)
                .point(this.point)
                .build();
    }



}
