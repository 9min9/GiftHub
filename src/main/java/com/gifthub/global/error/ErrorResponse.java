package com.gifthub.global.error;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Slf4j
public class ErrorResponse {

    private final MessageSource messageSource;

    public static ErrorResponse setMessageSource(MessageSource messageSource) {
        return new ErrorResponse(messageSource);
    }

    public List<ErrorDetail> getErrors(Errors errors) {
        ErrorResult errorResult = new ErrorResult(errors, messageSource, Locale.getDefault());

        List<ErrorDetail> errerList = errorResult.getErrors();

        for (ErrorDetail errorDetail : errerList) {
            log.error(errorDetail.getCode() + " | " + errorDetail.getField() + " | " + errorDetail.getObjectName() + " | " + errorDetail.getMessage());
        }

        return errerList;

    }


}
