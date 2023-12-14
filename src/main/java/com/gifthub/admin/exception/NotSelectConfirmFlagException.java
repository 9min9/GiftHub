package com.gifthub.admin.exception;

import com.gifthub.global.exception.BaseException;
import lombok.Getter;

@Getter
public class NotSelectConfirmFlagException extends BaseException {

    public NotSelectConfirmFlagException() {
        super.setField("isConfirm");
        super.setCode("NotSelect");
        super.setMessage("등록 유무를 선택해주세요");
    }


}
