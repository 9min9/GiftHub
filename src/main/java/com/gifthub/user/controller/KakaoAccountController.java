package com.gifthub.user.controller;

import com.gifthub.config.security.KakaoAuthenticationProvider;
import com.gifthub.config.security.KakaoAuthenticationToken;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.service.KakaoAccountService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/kakao/")
@RequiredArgsConstructor
public class KakaoAccountController {
    private final KakaoAccountService kakaoAccountService;
    private final UserService userService;
    private final UserAccountService commonUserService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;

    @RequestMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        String access_Token = "";
        KakaoUserDto userInfo = null;
        String jwtToken="";

        try {
            access_Token = kakaoAccountService.getKakaoAccessToken(code);   //kakao 인가 토큰을 사용하여 발급한 access token
            userInfo = kakaoAccountService.getKakaoUserInfo(access_Token);  //access token을 사용하여 kakao의 user info 발급

            System.out.println("loginController");
            System.out.println(code);
            System.out.println("KAT");
            System.out.println(access_Token);

            if(!commonUserService.duplicateEmail(userInfo.getEmail())) {
                userService.saveKakaoUser(userInfo);
            }

            /** 로그인 (jwt 토큰 발급)
             * 1. kakaoUserInfo의 accountId를 통해 kakaoAuthenticationToken 생성
             * 2. kakaoAuthenticationProvider를 통해 kakaoAuthenticationToken를 검증
             * 3. 인증이 완료되면 jwt 토큰을 발급
             * 4. jwt 토큰을 ResponseEntity를 통해 header로 전송*/

            KakaoAuthenticationToken kakaoAuthenticationToken = new KakaoAuthenticationToken(userInfo.getKakaoAccountId()); //1
            Authentication authentication = kakaoAuthenticationProvider.authenticate(kakaoAuthenticationToken);             //2

            if (authentication.isAuthenticated()) {     //3
                jwtToken = jwtTokenProvider.generateJwtToken(
                        UserDto.builder()
                                .email(userInfo.getEmail())
                                .kakaoAccountId(userInfo.getKakaoAccountId())
                                .loginType(LoginType.KAKAO.name()).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().header("Authorization", "Bearer "+jwtToken).build();     //4
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        kakaoAccountService.kakaoLogout((String) session.getAttribute("access_Token"));
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
        return "/";
    }
}
