package com.gifthub.user.exception;

import lombok.Getter;

@Getter

public class LogInFailException extends RuntimeException{

    private String code;
    private String message;


    LogInFailException() {

    }



}
