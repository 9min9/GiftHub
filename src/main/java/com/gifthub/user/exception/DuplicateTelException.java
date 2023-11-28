package com.gifthub.user.exception;

import lombok.Getter;

@Getter
public class DuplicateTelException extends RuntimeException {

    private String code;
    private String message;

    public DuplicateTelException(){
        this.code="duplicate.tel";
        this.message="이미 등록된 번호입니다.";

    }
}
