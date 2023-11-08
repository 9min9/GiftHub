package com.gifthub.payment.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DtoToMultiValueMapConverter {

    public static MultiValueMap<String, String> convert(ObjectMapper objectMapper, Object dto) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<Map<String, String>>() { });

            params.setAll(map);

            return params;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

}
