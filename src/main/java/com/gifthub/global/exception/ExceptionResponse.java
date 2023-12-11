package com.gifthub.global.exception;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ExceptionResponse {

    private Map<String, String> response = new HashMap<>();

    public Map<String, String> getException(String field, String code, String message) {
        this.response.put("field", field);

        if (field == null || field.isEmpty()) {
            this.response.put("code", code);
        } else {
            this.response.put("code", code + "." +field);
        }

        this.response.put("message", message);

        return response;
    }

}
