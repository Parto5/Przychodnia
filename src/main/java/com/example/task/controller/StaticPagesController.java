package com.example.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class StaticPagesController {

    @GetMapping("/o-przychodni")
    public String about(Model model) throws
            IOException {
        System.out.println("➡️ Wszedłem do /o-przychodni");
        ClassLoader classLoader = getClass().getClassLoader();
        try (var inputStream = classLoader.getResourceAsStream("data/o-przychodni.txt")){
            if (inputStream == null) {
                model.addAttribute("infoLines", List.of("Brak danych do wyświetlenia"));
            } else {
                List<String> lines = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream))
                        .lines().toList();
                model.addAttribute("infoLines", lines);
            }
        }
        return "o-przychodni";
    }

    @GetMapping("/kontakt")
    public String contact() {
        System.out.println("➡️ Wszedłem do /kontakt");
        return "kontakt";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/przychodnia";
    }
}
