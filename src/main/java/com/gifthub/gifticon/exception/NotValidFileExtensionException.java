package com.gifthub.gifticon.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NotValidFileExtensionException extends BaseException {
    public NotValidFileExtensionException() {
        super.setCode("NotValid");
        super.setField("fileExtension");
        super.setMessage("지원하지 않는 파일확장자 입니다");
    }
}
