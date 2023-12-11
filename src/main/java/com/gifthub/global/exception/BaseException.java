package com.gifthub.global.exception;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private String code;
    private String field;
    private String message;

}
