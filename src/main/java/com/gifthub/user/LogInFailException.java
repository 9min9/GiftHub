package com.gifthub.user;

import lombok.Getter;

@Getter

public class LogInFailException extends RuntimeException{

    private String code;
    private String message;


    LogInFailException() {

    }



}
