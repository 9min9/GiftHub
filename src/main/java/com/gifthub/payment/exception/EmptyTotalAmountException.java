package com.gifthub.payment.exception;

import com.gifthub.global.exception.BaseException;

public class EmptyTotalAmountException extends BaseException {

    public EmptyTotalAmountException() {
        super.setField("totalAmount");
        super.setCode("empty");
        super.setMessage("총 가격이 0입니다. 상품 추가 후 다시 시도해주세요.");
    }
}
