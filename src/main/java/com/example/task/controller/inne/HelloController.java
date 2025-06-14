package com.example.task.controller.inne;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
// testowy controller dla sprawdzenia czy w ogóle odpala front-end
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}