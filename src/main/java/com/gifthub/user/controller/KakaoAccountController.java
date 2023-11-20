package com.gifthub.user.controller;
import com.gifthub.user.KakaoUserJwtTokenProvider;
import com.gifthub.user.LogInFailException;
import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.exception.DuplicateEmailException;
import com.gifthub.user.service.CommonUserService;
import com.gifthub.user.service.KakaoAccountService;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller

@RequestMapping(value="/api/kakao/")
@RequiredArgsConstructor
public class KakaoAccountController {
    //http://localhost:8081/api/kakao/oauth?code=z70LayKZDGKkQ1db2aCnSzCBKf6ah0miXpavho_Lyd5YnChRJjAFDe6ULX8KPXPsAAABi6d6z_Gt1856Xp2T3g
//    /**
//     * 카카오 callback
//     * [GET] /api/kakao/login
//     */
    private final KakaoAccountService kakaoAccountService;
    private final CommonUserService commonUserService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final KakaoUserJwtTokenProvider kakaoUserJwtTokenProvider;

    @GetMapping("/login/index")
    public String loginPage() {
        return "/login-index";
    }

    @RequestMapping(value = "/signup")
    public String signup(@RequestParam("code") String code, HttpSession session) {
        String access_Token = kakaoAccountService.getKakaoAccessToken(code);
        KakaoUserDto userInfo = kakaoAccountService.getKakaoUserInfo(access_Token);

        try {
            commonUserService.duplicateEmailValidate(userInfo.getEmail());

            kakaoAccountService.save(
                    KakaoUserDto.builder()
                            .kakaoAccountId(userInfo.getKakaoAccountId())
                            .email(userInfo.getEmail())
                            .name(userInfo.getName())
                            .nickname(userInfo.getNickname())
                            .gender(userInfo.getGender())
                            .year(userInfo.getYear())
                            .date(userInfo.getDate())
                            .phoneNumber(userInfo.getPhoneNumber())
                            .point(0L)
                            .build());

            return "redirect:/";
        } catch (DuplicateEmailException e) {
            session.setAttribute("msg", "로그인실패");
            return "redirect:/";

        }

    }

    public Map login(String email, String kakaoAccountId) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoAccountId, email);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                String token = kakaoUserJwtTokenProvider.generateKakaoJwtToken(new KakaoUserDto(kakaoAccountId, email, null, "", "", "", null, null, 0l));
                Map<String, Object> map = new HashMap<>();
                map.put("token", token);
                return map;
            }
        } catch (LogInFailException e) {
            // 인증 예외 처리
        }
        return null;
    }


//    @PostMapping("/login")
//    public Map login(String email, String kakaoAccountId, String eamil, String nickname) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoAccountId,email);
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        System.out.println(authentication.isAuthenticated());
//
//        Map map = new HashMap();
//        boolean flag = authentication.isAuthenticated();
//
//        if (flag) {
//            String token = kakaoUserJwtTokenProvider.generateKakaoJwtToken(new KakaoUserDto(kakaoAccountId, email ,null,"","","",null,null,0l));
//            flag = true;
//            map.put("token", token);
//        }
//        map.put("flag", flag);
//        return map;
//    }

    @RequestMapping(value = "/login")
    public String login(@RequestParam("code") String code, HttpSession session, Model model) {
        String access_Token = kakaoAccountService.getKakaoAccessToken(code);
        KakaoUserDto userInfo = kakaoAccountService.getKakaoUserInfo(access_Token);
        Map<String, Object> loginToken = new HashMap<>();


        try {
            commonUserService.duplicateEmailValidate(userInfo.getEmail());

            kakaoAccountService.save(
                    KakaoUserDto.builder()
                            .kakaoAccountId(userInfo.getKakaoAccountId())
                            .email(userInfo.getEmail())
                            .name(userInfo.getName())
                            .nickname(userInfo.getNickname())
                            .gender(userInfo.getGender())
                            .year(userInfo.getYear())
                            .date(userInfo.getDate())
                            .phoneNumber(userInfo.getPhoneNumber())
                            .point(0L)
                            .build());

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo.getKakaoAccountId(), userInfo.getEmail());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                String token = kakaoUserJwtTokenProvider.generateKakaoJwtToken(
                        KakaoUserDto.builder().kakaoAccountId(userInfo.getKakaoAccountId()).email(userInfo.getEmail()).build());
                loginToken.put("token", token);
            }
            model.addAttribute("loginToken", loginToken);
            return "redirect:/";
        }


    }


    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        kakaoAccountService.kakaoLogout((String) session.getAttribute("access_Token"));
        session.removeAttribute("access_Token");
        session.removeAttribute("userId");
        return "/index";
    }

}
