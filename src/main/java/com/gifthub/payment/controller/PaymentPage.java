package com.gifthub.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class PaymentPage {

    @RequestMapping("payment/close")
    public String close() {
        return "/payment/close";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "/payment/checkout";
    }

}
