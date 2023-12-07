package com.gifthub.user.exception;

import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException {

    private String code;
    private String message;

    public DuplicateEmailException() {
        this.code = "Duplicate.email";
        this.message = "이메일이 중복되었습니다.";
    }
}



