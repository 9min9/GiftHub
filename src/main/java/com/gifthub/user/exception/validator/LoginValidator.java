package com.gifthub.user.exception.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;

@RequiredArgsConstructor
public class LoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Map<String, String> loginRequest = (Map<String, String>) target;

        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        if (email == null || email.isEmpty()) {
            errors.reject("NotBlank.email", "mes");
//            errors.rejectValue("email", "NotBlank.email", "");
        }

        if (password == null || password.isEmpty()) {
            errors.reject( "NotBlank.password", "");
        }
    }
}
