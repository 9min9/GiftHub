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

    @GetMapping("/gifticon/list/{option}")
    public String list(@PathVariable String option) {
        if (option.equals("all"))
            return "gifticon/list";
        else if (option.equals("beverage")) {
            return "gifticon/list-search";
        }
        else {
            return "gifticon/list";
        }
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
