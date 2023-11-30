package com.gifthub.admin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NotSelectFlagException extends RuntimeException{
    private String code;
    private String message;
}
