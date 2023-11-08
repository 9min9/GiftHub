package com.gifthub.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kakao/pay")
public class KakaoPage {

    @GetMapping
    public String payment() {
        return "/payment/payment";
    }


}
