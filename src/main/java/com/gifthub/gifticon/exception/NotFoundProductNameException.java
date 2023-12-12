package com.gifthub.gifticon.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NotFoundProductNameException extends BaseException {

    public NotFoundProductNameException() {
        super.setField("product");
        super.setCode("NotFound");
        super.setMessage("존재하지 않는 상품입니다");
    }

}
