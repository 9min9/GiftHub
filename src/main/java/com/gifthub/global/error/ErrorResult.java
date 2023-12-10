package com.gifthub.global.error;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Locale;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class ErrorResult {
    private List<ErrorDetail> errors;

    @Builder
    public ErrorResult(Errors errors, MessageSource messageSource, Locale locale) {
        this.errors = errors.getFieldErrors().stream()
                .map(error -> ErrorDetail.builder()
                        .fieldError(error).messageSource(messageSource).locale(locale).build()).toList();
    }
}
