package com.gifthub.gifticon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GifticonPageController {

    @GetMapping("/gifticon/add")
    public String add() {
        return "gifticon/add";
    }
}
