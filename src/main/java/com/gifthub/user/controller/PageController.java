package com.gifthub.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    @GetMapping("/login/index")
    public String login(){
        return "/login-index";
    }




}
