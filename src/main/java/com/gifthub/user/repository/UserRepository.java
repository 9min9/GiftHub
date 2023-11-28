package com.gifthub.user.repository;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.NaverUser;
import com.gifthub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    Optional<User> findByTel(String tel);

    Optional<KakaoUser> findByKakaoAccountId(String kakaoAccountId);
    Optional<NaverUser> findByNaverId(String naverAccountId);
}
