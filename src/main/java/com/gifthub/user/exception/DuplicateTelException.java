package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class DuplicateTelException extends BaseException {
    public DuplicateTelException(){
        super.setField("tel");
        super.setCode("Duplicate");
        super.setMessage("이미 등록된 번호입니다");
    }
}
