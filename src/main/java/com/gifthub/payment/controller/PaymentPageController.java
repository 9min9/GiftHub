package com.gifthub.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentPageController {

    @RequestMapping("/close")
    public String close() {
        return "/payment/close";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "/payment/checkout";
    }

    @GetMapping("orders")
    public String orders() {
        return "/payment/orders";
    }

    @GetMapping("/kakao")
    public String payment() {
        return "/payment/payment";
    }


}
