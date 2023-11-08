package com.gifthub.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gifthub.payment.dto.kakao.*;
import com.gifthub.payment.util.DtoToMultiValueMapConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter // 테스트용
public class KakaoPayService {

    private final ObjectMapper objectMapper;

    @Value("${kakaoKey}")
    private String kakaoKey;

    public KakaoPayReadyResponseDto ready(final KakaoPayReadyRequestDto requestDto) {
        MultiValueMap<String, String> convertedRequestDto = DtoToMultiValueMapConverter.convert(objectMapper, requestDto);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(convertedRequestDto, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://kapi.kakao.com/v1/payment/ready";

        Map<String, String> map = restTemplate.postForObject(url, request, Map.class);

        KakaoPayReadyResponseDto responseDto = KakaoPayReadyResponseDto.builder()
                .tid(map.get("tid"))
                .next_redirect_pc_url(map.get("next_redirect_pc_url"))
                .build();

        return responseDto;
    }

    public KakaoPayApproveResponseDto approve(final KakaoPayApproveRequestDto requestDto) {
        MultiValueMap<String, String> convertedRequestDto = DtoToMultiValueMapConverter.convert(objectMapper, requestDto);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(convertedRequestDto, getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://kapi.kakao.com/v1/payment/approve";

        Map<String, String> map = restTemplate.postForObject(url, request, Map.class);

        KakaoPayApproveResponseDto responseDto = KakaoPayApproveResponseDto.builder()
                .aid(map.get("aid"))
                .tid(map.get("tid"))
                .cid(map.get("cid"))
                .partner_order_id(map.get("partner_order_id"))
                .partner_user_id(map.get("partner_user_id"))
                .payment_method_type(map.get("payment_method_type"))
                .amount(objectMapper.convertValue(map.get("amount"), Amount.class))
                .item_name(map.get("item_name"))
                .item_code(map.get("item_code"))
                .quantity(1)
                .created_at(LocalDateTime.parse(map.get("created_at")))
                .approved_at(LocalDateTime.parse(map.get("approved_at")))
                .payload(map.get("payload"))
                .build();

        return responseDto;
    }


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "KakaoAK " + kakaoKey);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }

}
