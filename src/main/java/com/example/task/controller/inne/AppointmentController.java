package com.example.task.controller.inne;

import com.example.task.model.Appointment;
import com.example.task.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getOpenAppointmentsByDoctor(@PathVariable Long doctorId) {
        return appointmentService.getOpenAppointmentsForDoctor(doctorId);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
}
