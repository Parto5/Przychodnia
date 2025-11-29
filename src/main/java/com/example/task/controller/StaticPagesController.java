package com.example.task.controller;

import com.example.task.model.User;
import com.example.task.model.UserRole;
import com.example.task.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class StaticPagesController {

    private final UserService userService;

    public StaticPagesController(UserService userService) {
        this.userService = userService;
    }

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean loggedIn = authentication.isAuthenticated() && !username.equals("anonymousUser");

        //dodane, aby można było przeglądać niezalogowanym
        User user;
        String role = "GUEST";
        String realName = "Niezalogowany";

        if (loggedIn) {
            user = userService.findByUsername(username);

            if (user.getRole() == UserRole.PATIENT) {
                realName = user.getPatient().getName();
                role = "PATIENT";
            } else {
                realName = user.getDoctor().getName();
                role = "DOCTOR";
            }
        }
        model.addAttribute("username", loggedIn ? username : null);
        model.addAttribute("realname", realName);
        model.addAttribute("role", role);

        return "o-przychodni";
    }

    @GetMapping("/kontakt")
    public String contact(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean loggedIn = authentication.isAuthenticated() && !username.equals("anonymousUser");

        //dodane, aby można było przeglądać niezalogowanym
        User user;
        String role = "GUEST";
        String realName = "Niezalogowany";

        if (loggedIn) {
            user = userService.findByUsername(username);

            if (user.getRole() == UserRole.PATIENT) {
                realName = user.getPatient().getName();
                role = "PATIENT";
            } else {
                realName = user.getDoctor().getName();
                role = "DOCTOR";
            }
        }
        model.addAttribute("username", loggedIn ? username : null);
        model.addAttribute("realname", realName);
        model.addAttribute("role", role);

        System.out.println("➡️ Wszedłem do /kontakt");
        return "kontakt";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/przychodnia";
    }
}
