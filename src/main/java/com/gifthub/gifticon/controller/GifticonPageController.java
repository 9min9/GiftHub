package com.gifthub.gifticon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GifticonPageController {

    @GetMapping("/gifticon/add")
    public String add() {
        return "gifticon/add";
    }

    @GetMapping("/user/my-gifticon")
    public String myGifticonPage() {
        return "gifticon/my-gifticon";
    }

    @GetMapping("/gifticons")
    public String list() {
        return "gifticon/list";
    }

    @GetMapping("/gifticons/{type}")
    public String gifticonByType(@PathVariable("type") String type) {
        return "/gifticon/list-search";
    }

//    @GetMapping("/gifticon/list/{category}")
//    public String listCategory(@PathVariable("category") String category) {
//        return "gifticon/list-search";
//    }

//    @GetMapping("/gifticon/list/{brand}")
//    public String listBrand() {
//        return "gifticon/list";
//    }

}
