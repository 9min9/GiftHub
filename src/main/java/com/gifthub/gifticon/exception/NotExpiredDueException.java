package com.gifthub.gifticon.exception;

import com.gifthub.global.exception.BaseException;

public class NotExpiredDueException extends BaseException {
    public NotExpiredDueException(){
        super.setCode("NotExpired");
        super.setField("due");
        super.setMessage("유효기간이 지났습니다");
    }
}
