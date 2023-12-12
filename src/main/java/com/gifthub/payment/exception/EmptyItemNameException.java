package com.gifthub.payment.exception;

import com.gifthub.global.exception.BaseException;

public class EmptyItemNameException extends BaseException {

    public EmptyItemNameException() {
        super.setField("itemName");
        super.setCode("Empty");
        super.setMessage("상품 이름이 없습니다. 상품 선택 후 다시 결제해주세요.");
    }

}
