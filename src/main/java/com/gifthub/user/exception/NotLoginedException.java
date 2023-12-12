package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;

public class NotLoginedException extends BaseException {

    public NotLoginedException() {
        super.setCode("NotLogined");
        super.setMessage("로그인을 해주세요.");
    }
}
