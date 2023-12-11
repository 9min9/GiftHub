package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MismatchPasswordAndConfirmPassword extends BaseException {

    public MismatchPasswordAndConfirmPassword(){
        super.setField("password");
        super.setCode("MisMatch");
        super.setMessage("비밀번호 확인이 일치하지 않습니다");
    }
}
