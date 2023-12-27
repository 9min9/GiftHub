package com.gifthub.user.controller;

import com.gifthub.config.jwt.JwtContext;
import com.gifthub.config.jwt.KakaoAuthenticationProvider;
import com.gifthub.config.jwt.SocialAuthenticationToken;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.TokenInfo;
import com.gifthub.user.service.KakaoAccountService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/kakao")
@RequiredArgsConstructor
@Slf4j
public class KakaoAccountController {
    private final KakaoAccountService kakaoAccountService;
    private final UserService userService;
    private final UserAccountService commonUserService;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;

    @RequestMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestParam("code") String code) {
        String kakaoAccessToken = "";
        KakaoUserDto kakaoUserInfo = null;
        TokenInfo tokenInfo = null;

        try {
            kakaoAccessToken = kakaoAccountService.getKakaoAccessToken(code);
            kakaoUserInfo = kakaoAccountService.getKakaoUserInfo(kakaoAccessToken);

            if (!commonUserService.isDuplicateEmail(kakaoUserInfo.getEmail())) {
                kakaoUserInfo.setPoint(0L);
                userService.saveKakaoUser(kakaoUserInfo);
            }

            SocialAuthenticationToken kakaoAuthenticationToken = new SocialAuthenticationToken(kakaoUserInfo.getKakaoAccountId());
            Authentication authentication = kakaoAuthenticationProvider.authenticate(kakaoAuthenticationToken);

            if (authentication.isAuthenticated()) {
                KakaoUserDto findKakaoUserDto = kakaoAccountService.getKakaoUserByAccountId(kakaoUserInfo.getKakaoAccountId());
                tokenInfo = jwtTokenProvider.generateTokenInfo(findKakaoUserDto.toUserDto());
            }

        } catch (Exception e) {
            log.error("KakaoAccountController | login | " +e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenInfo.getAccessToken())
                .body(tokenInfo);
    }

    @RequestMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpSession session) {

        try {
            Object accessToken = session.getAttribute("kakao_access_token");
            kakaoAccountService.kakaoLogout((String) session.getAttribute("kakao_access_token"));
            session.removeAttribute("kakao_access_Token");

            JwtContext.clear();
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            log.error("KakaoAccountController | logout | " +e);
            return ResponseEntity.badRequest().build();
        }
    }
}
