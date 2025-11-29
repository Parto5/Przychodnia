package com.example.task.controller;

import com.example.task.model.*;
import com.example.task.service.DoctorService;
import com.example.task.service.PatientService;
import com.example.task.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public RegistrationController(UserService userService, DoctorService doctorService, PatientService patientService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("name") String name,
                               @RequestParam(value = "specialization", required = false) String specialization,
                               HttpServletRequest request) throws ServletException {

        if (user.getRole() == UserRole.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setName(name);
            doctor.setSpecialization(specialization != null ? specialization : "Ogólna");
            doctor = doctorService.save(doctor);
            user.setDoctor(doctor);
        } else if (user.getRole() == UserRole.PATIENT) {
            Patient patient = new Patient();
            patient.setName(name);
            patient = patientService.save(patient);
            user.setPatient(patient);
        }

        userService.save(user);

        // Wyloguj aktualnie zalogowanego użytkownika przed ponownym loginem
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            request.logout();
        }

        request.login(user.getUsername(), user.getPassword());

        return "redirect:/przychodnia?registered=1";
    }
}
