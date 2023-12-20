package com.gifthub.user.controller;

import com.gifthub.config.jwt.NaverAuthenticationProvider;
import com.gifthub.config.jwt.SocialAuthenticationToken;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.NaverTokenDto;
import com.gifthub.user.dto.NaverUserDto;
import com.gifthub.user.dto.TokenInfo;
import com.gifthub.user.service.NaverAccountService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver")
@Slf4j
public class NaverAccountConotroller {
    private final NaverAccountService naverAccountService;
    private final UserAccountService commonUserservice;
    private final UserService userService;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final NaverAuthenticationProvider naverAuthenticationProvider;

    @RequestMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        NaverUserDto naverUserInfo = null;
        TokenInfo tokenInfo = null;
        String token = "";

        try {
            NaverTokenDto naverAccessTokenDto = naverAccountService.getNaverAccessToken(code);
            String naverAccessToken = naverAccessTokenDto.getAccess_token();
            naverUserInfo = naverAccountService.getNaverUserInfo(naverAccessToken);

            if (!commonUserservice.isDuplicateEmail(naverUserInfo.getEmail())) {
                naverUserInfo.setPoint(0L);
                userService.saveNaverUser(naverUserInfo);
            }

            SocialAuthenticationToken socialAuthenticationToken = new SocialAuthenticationToken(naverUserInfo.getNaverId());
            Authentication authentication = naverAuthenticationProvider.authenticate(socialAuthenticationToken);

            if (authentication.isAuthenticated()) {
                NaverUserDto findNaverUserDto = naverAccountService.getNaverUserByNaverId(naverUserInfo.getNaverId());
                tokenInfo = jwtTokenProvider.generateTokenInfo(findNaverUserDto.toUserDto());
            }

        } catch (Exception e) {
            log.error("NaverAccountController | login " +e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenInfo.getAccessToken())
                .body(tokenInfo);
    }
}





