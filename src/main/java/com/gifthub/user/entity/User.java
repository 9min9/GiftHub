package com.gifthub.user.entity;

import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.global.BaseTimeEntity;
import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.enumeration.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "login_type")
@Table(name = "users")
@SuperBuilder
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;
    private String name;
    private String nickname;
    private String gender;
    private LocalDate year;
    private LocalDate birthDate;
    private String tel;
    private Long point;

    @Enumerated(STRING)
    private UserType userType;

//    @Enumerated(STRING)
//    private LoginType loginType;

    @OneToMany(mappedBy ="user")
    private List<Gifticon> gifticons = new ArrayList<Gifticon>();

    public Long usePoint(Long price) {
        this.point -= price;
        return this.point;
    }

    public Long plusPoint(Long price) {
        this.point += price;
        return this.point;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(userType.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return null;
    }


    public UserDto toDto() {
        return UserDto.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .userType(this.userType.name())
                .year(this.year)
                .date(this.birthDate)
                .tel(this.tel)
                .point(this.point)
                .build();
    }

    public KakaoUserDto toKakaoUserDto() {
//        KakaoUserDto.builder()
        return null;
    }








}
