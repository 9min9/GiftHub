package com.gifthub.gifticon.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;


@Getter
public class NotEmptyBrandNameException extends BaseException {

    public NotEmptyBrandNameException() {
        super.setField("brandName");
        super.setCode("NotEmpty");
        super.setMessage("브랜드를 입력해주세요");
    }

}
