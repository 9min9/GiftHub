package com.gifthub.event.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attendances")
public class AttendancePageController {

    @GetMapping
    public String attendances() {
        return "/attendance/attendance";
    }

}
