package com.gifthub.point.exception;

import com.gifthub.global.exception.BaseException;

public class NotFoundGifticonException extends BaseException {

    public NotFoundGifticonException() {
        super.setField("gifticon");
        super.setCode("NotFound");
        super.setMessage("없는 기프티콘 번호입니다. 존재하는 기프티콘을 구매해주세요.");
    }

}
