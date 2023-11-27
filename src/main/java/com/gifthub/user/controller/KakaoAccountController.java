package com.gifthub.user.controller;

import com.gifthub.config.jwt.KakaoAuthenticationProvider;
import com.gifthub.config.jwt.SocialAuthenticationToken;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.service.KakaoAccountService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/kakao")
@RequiredArgsConstructor
public class KakaoAccountController {
    private final KakaoAccountService kakaoAccountService;
    private final UserService userService;
    private final UserAccountService commonUserService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;
    
    @RequestMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestParam("code") String code) {
        String kakaoAccessToken = "";
        KakaoUserDto kakaoUserInfo = null;
        String token = "";

        try {
            kakaoAccessToken = kakaoAccountService.getKakaoAccessToken(code);   //kakao 인가 토큰을 사용하여 발급한 access token
            kakaoUserInfo = kakaoAccountService.getKakaoUserInfo(kakaoAccessToken);  //access token을 사용하여 kakao의 user info 발급

            System.out.println("loginController");
            System.out.println(code);
            System.out.println("KAT");
            System.out.println(kakaoAccessToken);

            if (!commonUserService.duplicateEmail(kakaoUserInfo.getEmail())) {
                kakaoUserInfo.setPoint(0L);
                userService.saveKakaoUser(kakaoUserInfo);
            }


            SocialAuthenticationToken kakaoAuthenticationToken = new SocialAuthenticationToken(kakaoUserInfo.getKakaoAccountId());
            Authentication authentication = kakaoAuthenticationProvider.authenticate(kakaoAuthenticationToken);

            if (authentication.isAuthenticated()) {
                KakaoUserDto findKakaoUserDto = kakaoAccountService.getKakaoUserByAccountId(kakaoUserInfo.getKakaoAccountId());

                token = jwtTokenProvider.generateJwtToken(
                        UserDto.builder()
                                .id(findKakaoUserDto.getId())
                                .email(kakaoUserInfo.getEmail())
                                .kakaoAccountId(kakaoUserInfo.getKakaoAccountId())
                                .loginType(LoginType.KAKAO.name()).build());

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    @RequestMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpSession session) {

        try {
            Object accessToken = session.getAttribute("kakao_access_token");
            System.out.println("logout");
            System.out.println((String) accessToken);

            kakaoAccountService.kakaoLogout((String) session.getAttribute("kakao_access_token"));
            session.removeAttribute("kakao_access_Token");

            return ResponseEntity.ok().build();

        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
