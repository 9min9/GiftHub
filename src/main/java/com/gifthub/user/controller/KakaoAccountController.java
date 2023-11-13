package com.gifthub.user.controller;
import com.gifthub.user.LogInFailException;
import com.gifthub.user.service.KakaoAccountService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/kakao")
@RequiredArgsConstructor
public class KakaoAccountController {

    private final KakaoAccountService kakaoAccountService;


    @PostMapping("url")
    public ResponseEntity<Object> methodName (@Valid Object params, BindingResult bindingResult) {
        try {

        } catch (Exception e) {     //사용자에게 보여줄 에러가 있다면
            bindingResult.reject("error", "errorMsg");
        } finally {
            if (bindingResult.hasErrors()) {        //만약 에러가 있다면
                return ResponseEntity.badRequest().body(bindingResult);     //ajax에 400코드를 보내고 우리가 처리한 에러 내용을 보냄
            }

            return ResponseEntity.ok().body("본인이 전송할 DTO");     //에러가 없다면 AJAX에 200코드를 보내고 내가 전송할 DTO를 BODY에 담아서 보냄
//            return ResponseEntity.ok().build();                  //view에 response를 할 것이 없다면 ok().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login (@Valid @RequestParam("code") String code, HttpSession session, BindingResult bindingResult) {
        String access_Token = kakaoAccountService.getAccessToken(code);
        HashMap<String , Object> userInfo = new HashMap<>();
        try {
            userInfo = kakaoAccountService.getUserInfo(access_Token);

            //TODO : JWT Token 방식으로 변경하기
            if(userInfo.get("email") !=null){
                session.setAttribute("userId",userInfo.get("email"));
                session.setAttribute("access_Token",access_Token);

            }
            System.out.println("code : " + code);


        } catch(LogInFailException e){
            bindingResult.reject("login.exception","");

        }
        finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult);

            }

            return ResponseEntity.ok().body(userInfo);

        }


    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session, BindingResult bindingResult) {
        try {
            kakaoAccountService.kakaoLogoutV2((String)session.getAttribute("access_Token"));
            session.removeAttribute("access_Token");
            session.removeAttribute("userId");

        } catch (IOException e) {
            bindingResult.reject("IOE", "IOE Exception 발생");

        } catch (LogInFailException e) {
            bindingResult.rejectValue("code", e.getCode());
            bindingResult.rejectValue("message", e.getMessage());
            bindingResult.reject("logout.exception", "");
        }

        finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult);
            }
            return ResponseEntity.ok().build();
        }
    }
}
