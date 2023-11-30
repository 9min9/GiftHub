package com.gifthub.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping("/index")
    public String adminIndex() {
        return "admin/admin-index";
    }

    @GetMapping("/product/config")
    public String productConfig() {
        return "admin/product-config";
    }

    @GetMapping("/user/config")
    public String userConfig() {
        return "admin/user-config";
    }

}
