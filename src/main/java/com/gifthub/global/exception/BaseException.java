package com.gifthub.global.exception;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private String field;
    private String code;
    private String message;

}
