package com.gifthub.gifticon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class NotFoundProductNameException extends RuntimeException{
    private String code;
    private String message;

    public NotFoundProductNameException() {
        code = "NotFound";
        message = "상품 이름이 존재하지 않습니다";
    }

}
