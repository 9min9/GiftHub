package com.gifthub.payment.exception;

import com.gifthub.global.exception.BaseException;

public class PaidIdMismatchException extends BaseException {

    public PaidIdMismatchException() {
        super.setField("paidId");
        super.setCode("Mismatch");
        super.setMessage("결제시도 아이디와 로그인 아이디가 다릅니다. 재로그인 후 다시 시도해주세요.");
    }
}
