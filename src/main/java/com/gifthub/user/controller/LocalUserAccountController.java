package com.gifthub.user.controller;

import com.gifthub.config.jwt.LocalUserAuthenticationProvider;
import com.gifthub.config.jwt.SocialAuthenticationToken;
import com.gifthub.global.error.ErrorResponse;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.global.exception.RequiredFieldException;
import com.gifthub.global.success.SuccessResponse;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.LocalUserDto;
import com.gifthub.user.dto.SignupRequest;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.enumeration.LoginType;
import com.gifthub.user.exception.*;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private final ExceptionResponse exceptionResponse;
    private final SuccessResponse successResponse;

    @PostMapping("/submit")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        System.out.println("Submit Controller");

        try {
            if (userAccountService.validateDuplicateEmail(signupRequest.getEmail())) {
                userService.saveLocalUser(signupRequest.toLocalUserDto());

            }
        } catch (DuplicateEmailException e) {
            bindingResult.rejectValue(e.getField(), e.getCode(), e.getMessage());

        } finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));
            }

            return ResponseEntity.ok(Collections.singletonMap("status", "200"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> request, Errors errors) {
        Map<String, String> result = new HashMap<>();
        System.out.println("LoginController");
        String token = "";

        try {
            String email = request.get("email");
            String password = request.get("password");

            System.out.println("email:" + email);
            System.out.println("password:" + password);
//            LoginValidator loginValidator = new LoginValidator();
//            loginValidator.validate(request, errors);

            if (email == null || email.isEmpty()) {
//                errors.rejectValue("email","NotBlank.email");
//                errors.reject("NotBlank");
                throw new NotFoundUserException();
//                errors.rejectValue("email", "NotBlank.email", "");
            }

            if (password == null || password.isEmpty()) {
//                errors.rejectValue("password", "NotBlank.password", "");
            }

            //todo : empty 일 때 reject 하고 리턴해야 함
            LocalUserDto localUserInfo = localUserService.getLocalUserByEmail(email);


            if (localUserInfo == null) {
                throw new NotFoundUserException();
            }

            if (userAccountService.isLogin(email, password)) {
                SocialAuthenticationToken LocalUserAuthenticationToken = new SocialAuthenticationToken(email);
                Authentication authentication = localUserAuthenticationProvider.authenticate(LocalUserAuthenticationToken);
                System.out.println("testauth:" + authentication);

                if (authentication.isAuthenticated()) {
                    LocalUserDto findLocalUserDto = localUserService.getLocalUserByEmail(localUserInfo.getEmail());

                    token = jwtTokenProvider.generateJwtToken(
                            UserDto.builder()
                                    .id(findLocalUserDto.getId())
                                    .email(localUserInfo.getEmail())
                                    .id(localUserInfo.getId())
                                    .loginType(LoginType.GIFT_HUB.name()).build());

                    System.out.println("testtoken:" + token);
                }
            }

        } catch (NotFoundUserException e) {
            log.error("Login Controller | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException("email", e.getCode(), e.getMessage()));

        } catch (Exception e) {
            log.error("Login Controller | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, "Exception", e.getMessage()));
        }

        if (errors.hasErrors()) {
            List<ObjectError> allErrors = errors.getAllErrors();
            List<FieldError> fieldErrors = errors.getFieldErrors();

            for (ObjectError allError : allErrors) {
                System.out.println(allError.getClass() + " | " + allError.getObjectName() + " | " + allError.getCode() + " | ");
            }

            for (FieldError allError : fieldErrors) {
                System.out.println(allError.getClass() + " | " + allError.getObjectName() + " | " + allError.getCode() + " | " + allError.getField());
            }

            return ResponseEntity.badRequest().body(errorResponse.getErrors(errors));
        }

        return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
    }

    @PostMapping(value = "/validate/email")
    public ResponseEntity<Object> emailCheck(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            if(email.isEmpty() || email == null) {
                throw new RequiredFieldException();
            }

            if (userAccountService.isDuplicateEmail(email)) {
                throw new DuplicateEmailException();
            }

            return ResponseEntity.ok().body(successResponse.getSuccess("email", "사용가능한 이메일 입니다"));

        } catch (RequiredFieldException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException("email", e.getCode(), e.getMessage()));

        } catch (DuplicateEmailException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException("email", e.getCode(), e.getMessage()));

        }
    }

    @PostMapping(value = "/validate/password")
    public ResponseEntity<Object> passwordCheck(@RequestBody Map<String, String> request) {
        Map<String, String> result = new HashMap<>();

        try {
            String passwrod = request.get("password");
            String confirmPassword = request.get("confirmPassword");

            if (passwrod.isEmpty() || passwrod == null) {
                //todo : 필수 에러 던지고 catch 하기
//                throw new RequiredF
            }

            if (!userAccountService.isMatchPasswordAndConfirmPassword(passwrod, confirmPassword)) {
                throw new MismatchPasswordAndConfirmPassword();
            }

            return ResponseEntity.ok().body(successResponse.getSuccess("password", "비밀번호가 일치합니다"));

        } catch (MismatchPasswordAndConfirmPassword e) {
            log.error("passwordCheck | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

//    @PostMapping(value = "/confirmpassword/check")
//    public ResponseEntity<Object> passwordValid(@RequestBody Map<String, String> request) {
//        Map<String, String> result = new HashMap<>();
//
//        try {
//            String password = request.get("password");
//            String confirmPassword = request.get("confirmPassword");
//            result.put("field", "confirmPassword");
//
//            if (userAccountService.isMatchPasswordAndConfirmPassword(password, confirmPassword)) {
//                throw new MismatchPasswordAndConfirmPassword();
//
//            }
//            result.put("status", "success");
//            result.put("message", "비밀번호가 동일합니다.");
//
//            if (password.isEmpty() || password == null) {
//                result.put("message", "");
//            }
//
//            return ResponseEntity.ok().body(result);
//
//        } catch (MismatchPasswordAndConfirmPassword e) {
//            result.put("status", "error");
//            result.put("code", e.getCode());
//            result.put("message", e.getMessage());
//
//            return ResponseEntity.badRequest().body(result);
//        }
//    }

    @PostMapping(value = "/validate/nickname")
    public ResponseEntity<Object> nicknameValid(@RequestBody Map<String, String> request) {
        Map<String, String> result = new HashMap<>();

        try {
            String nickname = request.get("nickname");

            if (nickname.isEmpty() || nickname == null) {
                result.put("message", "");
            }

            if (userAccountService.isDuplicateNickname(nickname)) {
                throw new DuplicateNicknameException();
            }

            return ResponseEntity.ok().body(successResponse.getSuccess("nickname", "사용가능한 닉네임 입니다"));

        } catch (DuplicateNicknameException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

    @PostMapping(value = "/validate/tel")
    public ResponseEntity<Object> telValid(@RequestBody Map<String, String> request) {
        try {
            String tel = request.get("tel");

            if (tel.isEmpty() || tel == null) {
                throw new RequiredFieldException();
            }

            if (userAccountService.isDuplicateTel(tel)) {
                throw new DuplicateTelException();
            }
            // todo : 패턴 검증
            return ResponseEntity.ok().body(successResponse.getSuccess("tel", "사용 가능한 전화번호 입니다"));

        } catch (RequiredFieldException e) {
            e.setFieldException("tel", "전화번호를 입력해주세요");
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));

        } catch (DuplicateTelException e) {
            log.error("telValid | " +e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

}
