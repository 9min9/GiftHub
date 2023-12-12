package com.gifthub.gifticon.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NotEmptyPriceException extends BaseException {
    public NotEmptyPriceException() {
        super.setField("price");
        super.setCode("NotEmpty");
        super.setMessage("가격을 입력해주세요");
    }
}
