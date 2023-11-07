package com.gifthub.user.entity;

import com.gifthub.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 회원가입된 유저의 정보를 저장하는 엔티티
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    private Long id;

    private String username;
    private String password;
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    private String userType;
    private LocalDate birthDate;
    private String tel;

}
