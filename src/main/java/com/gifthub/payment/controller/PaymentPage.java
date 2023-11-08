package com.gifthub.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/payment")
public class PaymentPage {

    @RequestMapping("/close")
    public String close() {
        return "/payment/close";
    }

}
