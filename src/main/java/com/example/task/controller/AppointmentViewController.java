package com.example.task.controller;

import com.example.task.model.Appointment;
import com.example.task.model.User;
import com.example.task.model.UserRole;
import com.example.task.service.AppointmentService;
import com.example.task.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(("/przychodnia"))
public class AppointmentViewController {

    private final AppointmentService appointmentService;
    private final UserService userService;


    public AppointmentViewController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }


    @GetMapping
    public String getAppointmentsView(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        System.out.println("➡️ Wszedłem do /przychodnia");

        if (date == null) {
            date = LocalDate.of(2025, 6, 16);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        List<Appointment> appointments = appointmentService.getAppointmentsByDate(date);

        // 🔽 sortowanie: najpierw po nazwisku lekarza, potem po godzinie
        appointments.sort((a1, a2) -> {
            int compareDoctor = a1.getDoctor().getName().compareToIgnoreCase(a2.getDoctor().getName());
            if (compareDoctor != 0) return compareDoctor;
            return a1.getDateTime().compareTo(a2.getDateTime());
        });

        model.addAttribute("username", username);
        if (user.getRole() == UserRole.PATIENT){
            model.addAttribute("realname", user.getPatient().getName());
        } else {
            model.addAttribute("realname", user.getDoctor().getName());
        }
        model.addAttribute("role", user.getRole().name());
        model.addAttribute("uselrId", user.getId());
        model.addAttribute("appointments", appointments);
        model.addAttribute("selectedDate", date.toString());

        return "przychodnia";
    }

    @PostMapping("/{id}/reserve")
    public String reserveAppointment(@PathVariable Long id,
                                     @RequestParam String selectedDate,
                                     Authentication authentication) {
        String username = authentication.getName();
        appointmentService.reserveAppointmentByUsername(id, username);
        return "redirect:/przychodnia?date=" + selectedDate;
    }

    @PostMapping("/{id}/accept")
    public String acceptAppointment(@PathVariable Long id,
                                    @RequestParam String selectedDate) {
        appointmentService.acceptAppointment(id);
        return "redirect:/przychodnia?date=" + selectedDate;
    }

    @PostMapping("/{id}/release")
    public String releaseAppointment(@PathVariable Long id,
                                     @RequestParam String selectedDate){
        appointmentService.releaseAppointment(id);
        return "redirect:/przychodnia?date=" + selectedDate;
    }

    @GetMapping("/new")
    public String newAppointmentForm(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     Model model) {
        Appointment appointment = new Appointment();
        if (date != null) {
            // Ustawienie godziny np. na 10:00 domyślnie
            appointment.setDateTime(date.atTime(10, 0));
        }
        model.addAttribute("appointment", appointment);
        return "nowa_wizyta";
    }

    @PostMapping("/new")
    public String createAppointment(@ModelAttribute Appointment appointment, Authentication auth) {
        String username = auth.getName();
        appointmentService.createAppointmentForDoctor(appointment, username);
        return "redirect:/przychodnia?date=" + appointment.getDateTime().toLocalDate();
    }
}