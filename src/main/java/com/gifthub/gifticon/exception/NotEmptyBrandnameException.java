package com.gifthub.gifticon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotEmptyBrandnameException extends RuntimeException {
    private String code;
    private String message;
    private String status = "error";

}
