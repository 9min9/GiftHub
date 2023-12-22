package com.gifthub.payment.controller;

import com.gifthub.payment.dto.PaymentDto;
import com.gifthub.payment.service.PaymentService;
import com.gifthub.user.UserJwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    private final UserJwtTokenProvider userJwtTokenProvider;

    @GetMapping
    public Page<PaymentDto> list(Pageable pageable, @RequestHeader HttpHeaders headers) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        return paymentService.getAll(userId, pageable);
    }

}
