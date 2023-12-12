package com.gifthub.user.exception;

import com.gifthub.global.exception.BaseException;

public class IdMismatchException extends BaseException {

    public IdMismatchException() {
        super.setField("id");
        super.setCode("Mismatch");
        super.setMessage("로그인한 아이디와 요청한 데이터의 아이디가 맞지 않습니다. 재로그인 후 다시 시도하세요.");
    }
}
