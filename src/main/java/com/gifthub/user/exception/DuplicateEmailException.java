package com.gifthub.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateEmailException extends RuntimeException {
    private String code;
    private String message;


}



