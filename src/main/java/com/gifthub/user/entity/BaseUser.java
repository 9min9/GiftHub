package com.gifthub.user.entity;

import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.entity.enumeration.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

import static jakarta.persistence.EnumType.STRING;

@MappedSuperclass
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseUser extends BaseTimeEntity implements UserDetails {

    @Column(name = "email", unique = true)
    private String email;
    private String name;
    private String nickname;
    @Enumerated(STRING)
    private UserType userType;
    @Enumerated(STRING)
    private LoginType loginType;
    private String gender;
    private LocalDate year;
    private LocalDate birthDate;
    private String tel;
    private Long point;

    public Long usePoint(Long price){
        this.point-= price;
        return this.point;
    }

    public Long plusPoint(Long price){
        this.point += price;
        return this.point;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
