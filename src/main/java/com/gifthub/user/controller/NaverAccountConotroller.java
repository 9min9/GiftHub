package com.gifthub.user.controller;

import com.gifthub.config.jwt.KakaoAuthenticationToken;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.NaverTokenDto;
import com.gifthub.user.dto.NaverUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.MsgEntity;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.service.NaverAccountService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserJwtTokenProvider jwtTokenProvider;


    @RequestMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {

        NaverUserDto naverUserInfo = null;
        String token = "";

        try {
            //카카오서비스의  getKakaoAccessToken으로 코드를 가져와서 kakaoAccesToken에 담음
//            naverAccessToken = naverAccountService.getnaverAccessToken(code);   //kakao 인가 토큰을 사용하여 발급한 access token

            System.out.println("controller");
            NaverTokenDto naverAccessTokenDto = naverAccountService.getNaverAccessToken(code);
            String naverAccessToken = naverAccessTokenDto.getAccess_token();

            System.out.println("NC");
            System.out.println("code:"+ code);

            System.out.println("NAT");
            System.out.println( naverAccessTokenDto.getAccess_token());
            System.out.println( naverAccessTokenDto.getToken_type());

            naverUserInfo = naverAccountService.getNaverUserInfo(naverAccessToken);  //access token을 사용하여 kakao의 user info 발급
            System.out.println(naverUserInfo);

//            if (!commonUserservice.duplicateEmail(naverUserInfo.getEmail())) {
//                userService.saveNAverUser(naverUserInfo);
//            }


//            KakaoAuthenticationToken kakaoAuthenticationToken = new KakaoAuthenticationToken(kakaoUserInfo.getKakaoAccountId()); //1
//            Authentication authentication = kakaoAuthenticationProvider.authenticate(kakaoAuthenticationToken);             //2
//
//            if (authentication.isAuthenticated()) {     //3
//                KakaoUserDto findKakaoUserDto = kakaoAccountService.getKakaoUserByAccountId(kakaoUserInfo.getKakaoAccountId());
//
//                token = jwtTokenProvider.generateJwtToken(
//                        UserDto.builder()
//                                .id(findKakaoUserDto.getId())
//                                .email(kakaoUserInfo.getEmail())
//                                .kakaoAccountId(kakaoUserInfo.getKakaoAccountId())
//                                .loginType(LoginType.KAKAO.name()).build());
//            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return null;
//        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();     //4
    }
}





