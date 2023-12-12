package com.gifthub.gifticon.exception;


import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NotEmptyDueException extends BaseException {
    public NotEmptyDueException() {
        super.setField("due");
        super.setCode("NotEmpty");
        super.setMessage("유효기간을 입력해주세요");

    }


}
