package com.gifthub.user.exception;

import lombok.Getter;

@Getter
public class MismatchPasswordAndConfirmPassword extends RuntimeException{

    private String code;
    private String message;

    public MismatchPasswordAndConfirmPassword(){
        this.code="misMatch.password";
        this.message="비밀번호가 다릅니다";
    }
}
