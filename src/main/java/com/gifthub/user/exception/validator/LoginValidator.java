package com.gifthub.user.exception.validator;

import com.gifthub.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Map;

@RequiredArgsConstructor
public class LoginValidator implements Validator {
    private UserAccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Map<String, String> loginRequest = (Map<String, String>) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotBlank.email", "");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotBlank.password", "");

        // 추가 검증 로직
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        //todo: 검증 로직 작성
    }

}
