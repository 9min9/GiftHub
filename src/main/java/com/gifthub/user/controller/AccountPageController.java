package com.gifthub.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountPageController {

    @GetMapping("/signup")
    public String signup() {
        return "account/signup";
    }

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "account/my-profile";
    }

    @GetMapping("/edit/profile")
    public String editPage() {
        return "account/edit-profile";
    }

    @GetMapping("/wishlist")
    public String wishlist() {
        return "user/wishlist";
    }


}
