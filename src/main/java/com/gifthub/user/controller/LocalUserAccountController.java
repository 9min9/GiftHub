package com.gifthub.user.controller;

import com.gifthub.config.jwt.LocalUserAuthenticationProvider;
import com.gifthub.config.jwt.SocialAuthenticationToken;
import com.gifthub.global.util.ErrorResponse;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.LocalUserDto;
import com.gifthub.user.dto.SignupRequest;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.exception.*;
import com.gifthub.user.exception.validator.LoginValidator;
import com.gifthub.user.service.LocalUserService;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    private final ErrorResponse errorResponse;

    @PostMapping("/submit")
    public ResponseEntity<Object> signup(@Valid  @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        System.out.println("Submit Controller");

        try {
            userService.saveLocalUser(signupRequest.toLocalUserDto());

            if(userAccountService.isDuplicateEmail(signupRequest.getEmail())) {
                bindingResult.rejectValue("email", "Duplicate.email");
            }

        } catch (DuplicateEmailException e) {
            bindingResult.rejectValue("email", e.getCode());

        } finally {
            if(bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));
            }

            return ResponseEntity.ok(Collections.singletonMap("status", "200"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String , String> request, Errors errors){
        Map<String, String> result = new HashMap<>();
        System.out.println("LoginController");
        String token="";

        try {
            String email = request.get("email");
            String password = request.get("password");

            System.out.println("email:"+email);
            System.out.println("password:"+password);
            LoginValidator loginValidator = new LoginValidator();
            loginValidator.validate(request, errors);
            LocalUserDto localUserInfo = localUserService.getLocalUserByEmail(email);

            if (localUserInfo == null) {
                throw new NotFoundUserException();
            }

            if (userAccountService.isLogin(email, password)) {
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

        } catch (NotFoundUserException e) {
            log.error("Login Controller | " +e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } finally {
            if (errors.hasErrors()) {

            }

        }

        return ResponseEntity.ok().header( "Authorization", "Bearer " + token).build();
    }

    @PostMapping(value="/email/check")
    public ResponseEntity<Object> emailCheck(@RequestBody Map<String, String> request){
        Map<String,String> result = new HashMap<>();

        try {
            String email = request.get("email");
            result.put("field", "email");

            if (userAccountService.isDuplicateEmail(email)) {
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
            result.put("field", "confirmPassword");


            if (!userAccountService.isMatchPasswordAndConfirmPassword(passwrod, confirmPassword )) {
                throw new MismatchPasswordAndConfirmPassword();
            }

            result.put("status", "success");
            result.put("message", "비밀번호가 동일합니다.");

            if(passwrod.isEmpty() || passwrod==null){
                result.put("message","");
            }


            return ResponseEntity.ok().body(result);

        } catch (MismatchPasswordAndConfirmPassword e) {
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
            result.put("field", "confirmPassword");

            if (userAccountService.isMatchPasswordAndConfirmPassword(password, confirmPassword )) {
                throw new MismatchPasswordAndConfirmPassword();

            }
            result.put("status", "success");
            result.put("message", "비밀번호가 동일합니다.");

            if(password.isEmpty() || password ==null){
                result.put("message","");
            }

            return ResponseEntity.ok().body(result);

        } catch (MismatchPasswordAndConfirmPassword e) {
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

            result.put("field", "nickname");

            if (userAccountService.isDuplicateNickname(nickname )) {
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

            result.put("field", "tel");
            System.out.println("tel:"+tel);

            if (userAccountService.isDuplicateTel(tel)) {
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
