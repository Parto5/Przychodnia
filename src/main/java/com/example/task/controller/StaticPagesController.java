package com.example.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPagesController {

    @GetMapping("/o-przychodni")
    public String about() {
        return "o-przychodni";
    }

    @GetMapping("/kontakt")
    public String contact() {
        return "kontakt";
    }
}
