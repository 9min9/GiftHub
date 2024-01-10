package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class DuplicateEmailException extends BaseException {

    public DuplicateEmailException() {
        super.setField("email");
        super.setCode("Duplicate");
        super.setMessage("이메일이 중복되었습니다");
    }
}



