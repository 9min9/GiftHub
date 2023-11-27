package com.gifthub.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment/kakao")
public class KakaoPageController {

    @GetMapping
    public String payment() {
        return "/payment/payment";
    }


}
