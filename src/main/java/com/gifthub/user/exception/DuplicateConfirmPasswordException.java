package com.gifthub.user.exception;

import lombok.Getter;

@Getter
public class DuplicateConfirmPasswordException extends RuntimeException{

    private String code;
    private String message;

    public DuplicateConfirmPasswordException(){
        this.code="duplicate.code";
        this.message="비밀번호가 다릅니다";
    }
}
