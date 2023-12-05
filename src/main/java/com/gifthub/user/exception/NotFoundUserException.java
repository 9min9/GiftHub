package com.gifthub.user.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class NotFoundUserException extends RuntimeException{
    private String code;
    private String message;

    public NotFoundUserException() {
        this.code = "notFound.user";
        this.message = "존재하지 않는 회원입니다";
    }
}
