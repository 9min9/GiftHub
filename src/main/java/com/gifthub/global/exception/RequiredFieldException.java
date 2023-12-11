package com.gifthub.global.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RequiredFieldException extends BaseException {

    public RequiredFieldException(String field, String message) {
        super(field, "Required", message);
    }

    public void setFieldException(String field, String message) {
        super.setField(field);
        super.setCode("Required");
        super.setMessage(message);
    }
}
