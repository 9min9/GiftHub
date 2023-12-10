package com.gifthub.global.error;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Locale;

@AllArgsConstructor
public class ErrorResponse {

    private final MessageSource messageSource;

    public static ErrorResponse setMessageSource(MessageSource messageSource) {
        return new ErrorResponse(messageSource);
    }

    public List<ErrorDetail> getErrors(Errors errors) {
        ErrorResult errorResult = new ErrorResult(errors, messageSource, Locale.getDefault());

        List<ErrorDetail> errerList = errorResult.getErrors();

        for (ErrorDetail errorDetail : errerList) {
            System.out.println(errorDetail.getCode() + " | " + errorDetail.getField() + " | " + errorDetail.getObjectName() + " | " + errorDetail.getMessage());
        }

        return errerList;

    }


}
