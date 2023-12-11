package com.gifthub.global.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ExceptionResponse {
    private Map<String, String> response = new HashMap<>();

    public Map<String, String> getException(String field, String code, String message) {
        this.response.put("field", field);
        this.response.put("code", code);
        this.response.put("message", message);

        return response;
    }

}
