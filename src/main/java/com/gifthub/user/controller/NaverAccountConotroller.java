package com.gifthub.user.controller;

import com.gifthub.config.jwt.SocialAuthenticationToken;
import com.gifthub.config.jwt.NaverAuthenticationProvider;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.NaverTokenDto;
import com.gifthub.user.dto.NaverUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.service.NaverAccountService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver")
public class NaverAccountConotroller {
    private final NaverAccountService naverAccountService;
    private final UserAccountService commonUserservice;
    private final UserService userService;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final NaverAuthenticationProvider naverAuthenticationProvider;

    @RequestMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        NaverUserDto naverUserInfo = null;
        String token = "";

        try {
            NaverTokenDto naverAccessTokenDto = naverAccountService.getNaverAccessToken(code);
            String naverAccessToken = naverAccessTokenDto.getAccess_token();

            System.out.println("NC");
            System.out.println("code:" + code);
            System.out.println("NAT");
            System.out.println(naverAccessTokenDto.getAccess_token());

            naverUserInfo = naverAccountService.getNaverUserInfo(naverAccessToken);
            System.out.println(naverAccessTokenDto.getToken_type());
            System.out.println(naverUserInfo);

            if (!commonUserservice.duplicateEmail(naverUserInfo.getEmail())) {
                naverUserInfo.setPoint(0L);

                userService.saveNaverUser(naverUserInfo);
            }

            SocialAuthenticationToken socialAuthenticationToken = new SocialAuthenticationToken(naverUserInfo.getNaverId());
            Authentication authentication = naverAuthenticationProvider.authenticate(socialAuthenticationToken);
//            System.out.println("Controller auth");
//            System.out.println(authentication);

            if (authentication.isAuthenticated()) {
                NaverUserDto findNaverUserDto = naverAccountService.getNaverUserByNaverId(naverUserInfo.getNaverId());

                token = jwtTokenProvider.generateJwtToken(
                        UserDto.builder()
                                .id(findNaverUserDto.getId())
                                .email(findNaverUserDto.getEmail())
                                .kakaoAccountId(findNaverUserDto.getNaverId())
                                .loginType(LoginType.NAVER.name()).build());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }
}





