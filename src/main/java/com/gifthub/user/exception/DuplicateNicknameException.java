package com.gifthub.user.exception;

import lombok.Getter;

@Getter
public class DuplicateNicknameException extends RuntimeException {
    private String code;
    private String message;

    public DuplicateNicknameException() {
        this.code = "duplicate.nickname";
        this.message = "중복된 닉네임입니다.";
    }
}

