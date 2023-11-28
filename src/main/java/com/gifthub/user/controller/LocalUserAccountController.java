package com.gifthub.user.controller;

import com.gifthub.user.dto.UserDto;
import com.gifthub.user.exception.DuplicateConfirmPasswordException;
import com.gifthub.user.exception.DuplicateEmailException;
import com.gifthub.user.exception.DuplicateNicknameException;
import com.gifthub.user.exception.DuplicateTelException;
import com.gifthub.user.service.UserAccountService;
import com.gifthub.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class LocalUserAccountController {
    private final UserService userService;
    private final UserAccountService userAccountService;
    @PostMapping("/submit")
    public ResponseEntity<Object> signup(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        try {

            userService.saveLocalUser(userDto);

        } catch (DuplicateEmailException e) {
            bindingResult.reject(e.getCode(), e.getMessage());

        } finally {

            if(bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult);
            }

            return ResponseEntity.ok("{\"status\": \"success\"}");
        }
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

            String passwrod = request.get("password");
            String confirmPassword =request.get("confirmPassword");
            result.put("target", "confirmPassword");

            if (userAccountService.validatePassword(passwrod, confirmPassword )) {
                throw new DuplicateConfirmPasswordException();

            }
            result.put("status", "success");
            result.put("message", "비밀번호가 동일합니다.");

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

            return ResponseEntity.ok().body(result);

        } catch (DuplicateNicknameException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value="/tel/check")
    public ResponseEntity<Object> telvalid(@RequestBody Map<String, String> request){
        Map<String,String> result = new HashMap<>();

        try {

            String tel = request.get("tel");

            result.put("target", "tel");

            if (userAccountService.validateTel(tel)) {
                throw new DuplicateTelException();
            }
            result.put("status", "success");
            result.put("message", "사용가능한 번호입니다. 인증 버튼을 눌러주세요");

            return ResponseEntity.ok().body(result);

        } catch (DuplicateTelException e) {
            result.put("status", "error");
            result.put("code", e.getCode());
            result.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(result);
        }
    }

}
