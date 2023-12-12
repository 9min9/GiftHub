package com.gifthub.payment.exception;

import com.gifthub.global.exception.BaseException;

public class EmptyPgTokenException extends BaseException {

    public EmptyPgTokenException() {
        super.setField("pgToken");
        super.setCode("Empty");
        super.setMessage("오류가 발생했습니다. 재시도해주세요.");
    }
}
