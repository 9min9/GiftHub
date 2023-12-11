package com.gifthub.global.success;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class SuccessResponse {
    private Map<String, String> response = new HashMap<>();

    public Map<String, String> getSuccess(String field, String message) {
        this.response.put("field", field);
        this.response.put("message", message);
        this.response.put("status", "success");

        return response;
    }
}
