package com.gifthub.user.exception;

public class MismatchPasswordException extends RuntimeException{

    private String code;
    private String message;

    public MismatchPasswordException() {
        this.code = "misMatch.password";
        this.message = "비밀번호가 일치하지 않습니다";
    }

}
