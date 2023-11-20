package com.gifthub.user.repository;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoUserRepository extends JpaRepository<KakaoUser, String> {
    Optional<KakaoUser> findByEmail(String email);
}
