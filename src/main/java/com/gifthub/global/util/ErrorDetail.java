package com.gifthub.global.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@AllArgsConstructor(access = PRIVATE)
public class ErrorDetail {
    private Object objectName;
    private String field;
    private String code;
    private String message;

    @Builder
    public ErrorDetail(FieldError fieldError, MessageSource messageSource, Locale locale) {
        this.objectName = fieldError.getObjectName();
        this.field = fieldError.getField();
        this.code = fieldError.getCode()+ "."+fieldError.getField();
        this.message = messageSource.getMessage(fieldError, locale);
    }

}
