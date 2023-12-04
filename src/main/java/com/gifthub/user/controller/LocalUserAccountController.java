package com.gifthub.user.controller;

import com.gifthub.config.jwt.LocalUserAuthenticationProvider;
import com.gifthub.config.jwt.SocialAuthenticationToken;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.LocalUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.exception.DuplicateConfirmPasswordException;
import com.gifthub.user.exception.DuplicateEmailException;
import com.gifthub.user.exception.DuplicateNicknameException;
import com.gifthub.user.exception.DuplicateTelException;
import com.gifthub.user.service.LocalUserService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gifthub.user.entity.enumeration.UserType.ADMIN;


@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class LocalUserAccountController {

    private final UserService userService;
    private final UserAccountService userAccountService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final LocalUserAuthenticationProvider localUserAuthenticationProvider;
    private final LocalUserService localUserService;


    @PostMapping("/submit")
    public ResponseEntity<Object> signup(@Valid  @RequestBody LocalUserDto localUserDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            if(localUserDto.getEmail().equals("admin")) {
                localUserDto.setUserType(ADMIN);
            }

            localUserDto.setPoint(0L);
            userService.saveLocalUser(localUserDto);

        } catch (DuplicateEmailException e) {
            bindingResult.reject(e.getCode(), e.getMessage());

        } finally {
            if(bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult);
            }

            return ResponseEntity.ok("{\"status\": \"success\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String , String> request){
        Map<String, String> result = new HashMap<>();

        String password = request.get("password");
        System.out.println("password:"+password);
        String email = request.get("email");
        System.out.println("email:"+email);
        String token="";
        LocalUserDto localUserInfo = localUserService.getLocalUserByEmail(email);
        try {


            if (userAccountService.validateAccount(email, password)) {
                SocialAuthenticationToken LocalUserAuthenticationToken = new SocialAuthenticationToken(email);
                Authentication authentication = localUserAuthenticationProvider.authenticate(LocalUserAuthenticationToken);
                System.out.println("testauth:"+authentication);

                if (authentication.isAuthenticated()) {
                    LocalUserDto findLocalUserDto = localUserService.getLocalUserByEmail(localUserInfo.getEmail());

                    token = jwtTokenProvider.generateJwtToken(
                            UserDto.builder()
                                    .id(findLocalUserDto.getId())
                                    .email(localUserInfo.getEmail())
                                    .id(localUserInfo.getId())
                                    .loginType(LoginType.GIFT_HUB.name()).build());

                    System.out.println("testtoken:"+token);
                }
            }

        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().header( "Authorization", "Bearer " + token).build();
    }

    @PostMapping(value="/email/check")
    public ResponseEntity<Object> emailCheck(@RequestBody Map<String, String> request){
        Map<String,String> result = new HashMap<>();

        try {
            String email = request.get("email");
            result.put("target", "email");

            if (userAccountService.duplicateEmail(email)) {
                throw new DuplicateEmailException();
            }

            result.put("status", "success");
            result.put("message", "사용 가능한 이메일 입니다");

            if(email.isEmpty() || email==null){
                result.put("message","");
            }

            return ResponseEntity.ok().body(result);

        } catch (DuplicateEmailException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value="/password/check")
    public ResponseEntity<Object> passwordCheck(@RequestBody Map<String, String> request){
        Map<String,String> result = new HashMap<>();

        try {

            String passwrod = request.get("password");
            String confirmPassword =request.get("confirmPassword");
            result.put("target", "confirmPassword");


            if (userAccountService.validatePassword(passwrod, confirmPassword )) {
                throw new DuplicateConfirmPasswordException();

            }
            result.put("status", "success");
            result.put("message", "비밀번호가 동일합니다.");

            if(passwrod.isEmpty() || passwrod==null){
                result.put("message","");
            }


            return ResponseEntity.ok().body(result);

        } catch (DuplicateConfirmPasswordException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());


            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value="/confirmpassword/check")
    public ResponseEntity<Object> passwordValid(@RequestBody Map<String, String> request){
        Map<String,String> result = new HashMap<>();

        try {

            String password = request.get("password");
            String confirmPassword =request.get("confirmPassword");
            result.put("target", "confirmPassword");

            if (userAccountService.validatePassword(password, confirmPassword )) {
                throw new DuplicateConfirmPasswordException();

            }
            result.put("status", "success");
            result.put("message", "비밀번호가 동일합니다.");

            if(password.isEmpty() || password ==null){
                result.put("message","");
            }

            return ResponseEntity.ok().body(result);

        } catch (DuplicateConfirmPasswordException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value="/nickname/check")
    public ResponseEntity<Object> nicknameValid(@RequestBody Map<String, String> request){
        Map<String,String> result = new HashMap<>();

        try {

            String nickname = request.get("nickname");

            result.put("target", "nickname");

            if (userAccountService.validateNickname(nickname )) {
                throw new DuplicateNicknameException();
            }
            result.put("status", "success");
            result.put("message", "사용해도 되는 닉네임입니다.");

            if(nickname.isEmpty() || nickname==null){
                result.put("message","");
            }

            return ResponseEntity.ok().body(result);

        } catch (DuplicateNicknameException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value="/tel/check")
    public ResponseEntity<Object> telValid(@RequestBody Map<String, String> request){
        Map<String,String> result = new HashMap<>();

        try {

            String tel = request.get("tel");

            result.put("target", "tel");
            System.out.println("tel:"+tel);

            if (userAccountService.validateTel(tel)) {
                throw new DuplicateTelException();
            }
            result.put("status", "success");
            result.put("message", "등록되지 않은 번호입니다. ");

            if(tel.isEmpty() || tel==null){
                result.put("message","");
            }

            return ResponseEntity.ok().body(result);

        } catch (DuplicateTelException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(result);
        }
    }




}
