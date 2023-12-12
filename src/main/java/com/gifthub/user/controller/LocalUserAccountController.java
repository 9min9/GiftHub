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
@RequestMapping("/api/local")
public class LocalUserAccountController {

    private final UserService userService;
    private final UserAccountService userAccountService;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final LocalUserAuthenticationProvider localUserAuthenticationProvider;
    private final LocalUserService localUserService;
    private final ErrorResponse errorResponse;
    private final ExceptionResponse exceptionResponse;
    private final SuccessResponse successResponse;

    @PostMapping("/signup/submit")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        System.out.println("Submit Controller");

        try {
            userAccountService.validateDuplicateEmail(signupRequest.getEmail());

        } catch (DuplicateEmailException e) {
            bindingResult.rejectValue(e.getField(), e.getCode(), e.getMessage());

        } finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(errorResponse.getErrors(bindingResult));
            } else {
                userService.saveLocalUser(signupRequest.toLocalUserDto());
                return ResponseEntity.ok(Collections.singletonMap("status", "200"));
            }
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> request, Errors errors) {
        String token = "";

        try {
            String email = request.get("email");
            String password = request.get("password");

            if (email == null || email.isEmpty()) {
                throw new RequiredFieldException("email", "이메일을 입력해주세요");
            }

            if (password == null || password.isEmpty()) {
                throw new RequiredFieldException("password", "비밀번호를 입력해주세요");
            }

            if (userAccountService.isLogin(email, password)) {
                SocialAuthenticationToken LocalUserAuthenticationToken = new SocialAuthenticationToken(email);
                Authentication authentication = localUserAuthenticationProvider.authenticate(LocalUserAuthenticationToken);

                if (authentication.isAuthenticated()) {
                    LocalUserDto findLocalUserDto = localUserService.getLocalUserByEmail(email);

                    token = jwtTokenProvider.generateJwtToken(
                            UserDto.builder()
                                    .id(findLocalUserDto.getId())
                                    .email(findLocalUserDto.getEmail())
                                    .id(findLocalUserDto.getId())
                                    .loginType(LoginType.GIFT_HUB.name()).build());
                }
            }

        } catch (RequiredFieldException e) {
            log.error("Login Controller | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));

        } catch (NotFoundUserException e) {
            log.error("Login Controller | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException("email", e.getCode(), e.getMessage()));

        } catch (MismatchPasswordException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));

        } catch (Exception e) {
            log.error("Login Controller | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(null, "Exception", e.getMessage()));
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errorResponse.getErrors(errors));
        }

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(Collections.singletonMap("status", "200"));
    }

    @PostMapping(value = "/signup/validate/email")
    public ResponseEntity<Object> emailValid(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            if (email.isEmpty() || email == null) {
                throw new RequiredFieldException("email", "이메일을 입력해주세요");
            }

            if (userAccountService.isDuplicateEmail(email)) {
                throw new DuplicateEmailException();
            }

            return ResponseEntity.ok().body(successResponse.getSuccess("email", "사용가능한 이메일 입니다"));

        } catch (RequiredFieldException e) {
            log.error("emailValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));

        } catch (DuplicateEmailException e) {
            log.error("emailValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

    @PostMapping(value = "/signup/validate/password")
    public ResponseEntity<Object> passwordCheck(@RequestBody Map<String, String> request) {
        Map<String, String> result = new HashMap<>();

        try {
            String passwrod = request.get("password");
            String confirmPassword = request.get("confirmPassword");

            if (passwrod.isEmpty() || passwrod == null) {
                //todo : 필수 에러 던지고 catch 하기
                throw new RequiredFieldException("password", "비밀번호를 입력해주세요");
            }

            if (confirmPassword.isEmpty() || confirmPassword == null) {
                throw new RequiredFieldException("confirmPassword", "비밀번호 확인을 입력해주세요");
            }

            if (!userAccountService.isMatchPasswordAndConfirmPassword(passwrod, confirmPassword)) {
                throw new MismatchPasswordAndConfirmPassword();
            }

            return ResponseEntity.ok().body(successResponse.getSuccess("password", "비밀번호가 일치합니다"));

        } catch (RequiredFieldException e) {
            log.error("passwordValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));

        } catch (MismatchPasswordAndConfirmPassword e) {
            log.error("passwordValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

    @PostMapping(value = "/signup/validate/nickname")
    public ResponseEntity<Object> nicknameValid(@RequestBody Map<String, String> request) {
        Map<String, String> result = new HashMap<>();

        try {
            String nickname = request.get("nickname");

            if (nickname.isEmpty() || nickname == null) {
                throw new RequiredFieldException("nickname", "닉네임을 입력해주세요");
            }

            if (userAccountService.isDuplicateNickname(nickname)) {
                throw new DuplicateNicknameException();
            }

            return ResponseEntity.ok().body(successResponse.getSuccess("nickname", "사용가능한 닉네임 입니다"));

        } catch (RequiredFieldException e) {
            log.error("nicknameValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        } catch (DuplicateNicknameException e) {
            log.error("nicknameValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

    @PostMapping(value = "/signup/validate/tel")
    public ResponseEntity<Object> telValid(@RequestBody Map<String, String> request) {
        try {
            String tel = request.get("tel");

            if (tel.isEmpty() || tel == null) {
                throw new RequiredFieldException("tel", "전화번호를 입력해주세요");
            }

            if (userAccountService.isDuplicateTel(tel)) {
                throw new DuplicateTelException();
            }
            // todo : 패턴 검증
            return ResponseEntity.ok().body(successResponse.getSuccess("tel", "사용 가능한 전화번호 입니다"));

        } catch (RequiredFieldException e) {
            log.error("telValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));

        } catch (DuplicateTelException e) {
            log.error("telValid | " + e);
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

}
