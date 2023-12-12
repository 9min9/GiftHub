package com.gifthub.point.exception;

import com.gifthub.global.exception.BaseException;

public class NotEnoughPointException extends BaseException {

    public NotEnoughPointException() {
        super.setField("point");
        super.setCode("NotEnough");
        super.setMessage("포인트가 부족합니다. 포인트를 충전하고 다시 시도해주세요.");
    }
}
