package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NotFoundUserException extends BaseException {
    public NotFoundUserException() {
        super.setCode("NotFound.user");
        super.setMessage("존재하지 않는 회원입니다");
    }

}
