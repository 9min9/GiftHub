package com.gifthub.exception;

public class InvalidDueDate extends RuntimeException{
    public InvalidDueDate(String msg){
        super(msg);
    }
}
