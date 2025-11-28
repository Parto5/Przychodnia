package com.example.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class StaticPagesController {

    @GetMapping("/o-przychodni")
    public String about(Model model) throws
            IOException {
        System.out.println("➡️ Wszedłem do /o-przychodni");
        Path path = Paths.get("src/main/resources/static/data/o-przychodni.txt");
        List<String> lines = Files.readAllLines(path);
        model.addAttribute("infoLines", lines);
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
