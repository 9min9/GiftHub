package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class MismatchPasswordException extends BaseException {

    public MismatchPasswordException() {
        super.setField("password");
        super.setCode("MisMatch");
        super.setMessage("비밀번호가 일치하지 않습니다");
    }

}
