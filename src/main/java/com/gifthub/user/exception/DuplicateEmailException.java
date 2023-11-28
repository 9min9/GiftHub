package com.gifthub.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class DuplicateEmailException extends RuntimeException {

    private String code;
    private String message;

    public DuplicateEmailException() {
        this.code = "duplicate.email";
        this.message = "이메일이 중복되었습니다.";
    }
}



