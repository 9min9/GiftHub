package com.gifthub.user.controller;

import com.gifthub.user.dto.UserDto;
import com.gifthub.user.exception.DuplicateEmailException;
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











}
