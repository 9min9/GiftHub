package com.gifthub.user.controller;

import com.gifthub.user.service.NaverAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountPageController {

    private final NaverAccountService naverAccountService;

//    @GetMapping("/")
//    public String login(Model model){
//        model.addAttribute("naverUrl", naverAccountService.getNaverLogin());
//        return "index";
//    }



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
