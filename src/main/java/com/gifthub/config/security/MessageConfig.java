package com.gifthub.config.security;

import com.gifthub.global.error.ErrorResponse;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.global.success.SuccessResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/errors");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public ErrorResponse errorResponse(MessageSource messageSource) {
        return ErrorResponse.setMessageSource(messageSource);
    }

    @Bean
    public ExceptionResponse exceptionResponse() {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        return exceptionResponse;
    }

    @Bean
    public SuccessResponse successResponse() {
        SuccessResponse successResponse = new SuccessResponse();
        return successResponse;
    }

}
