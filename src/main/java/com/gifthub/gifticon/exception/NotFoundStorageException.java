package com.gifthub.gifticon.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NotFoundStorageException extends BaseException {
    public NotFoundStorageException() {
        super.setField("storage");
        super.setCode("NotFound");
        super.setMessage("저장된 기프티콘이 존재하지 않습니다");
    }

}
