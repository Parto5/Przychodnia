package com.example.task.service;

import com.example.task.model.Appointment;
import com.example.task.model.AppointmentStatus;
import com.example.task.model.Doctor;
import com.example.task.model.User;
import com.example.task.repository.AppointmentRepository;
import com.example.task.repository.DoctorRepository;
import com.example.task.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    public List<Appointment> getOpenAppointmentsForDoctor(Long doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        return doctor.map(value -> appointmentRepository.findByDoctorAndStatus(value, AppointmentStatus.Open))
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
    }

    public List<Appointment> getOpenAppointmentsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return appointmentRepository.findByDateTimeBetweenAndStatus(
                startOfDay, endOfDay, AppointmentStatus.Open);
    }
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return appointmentRepository.findByDateTimeBetween(start, end);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void reserveAppointmentByUsername(Long appointmentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!"PATIENT".equals(user.getRole().name())) {
            throw new RuntimeException("Only patients can reserve appointments.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.Open) {
            throw new RuntimeException("Appointment is not available.");
        }

        appointment.setStatus(AppointmentStatus.Reserved);
        appointment.setReservedBy(user.getPatient()); // zakładamy, że user.getPatient() != null
        appointmentRepository.save(appointment);
    }

    public void acceptAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.Reserved) {
            throw new RuntimeException("Only reserved appointments can be accepted.");
        }

        appointment.setStatus(AppointmentStatus.Confirmed);
        appointmentRepository.save(appointment);
    }

    public void releaseAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.Open);
        appointment.setReservedBy(null);
        appointmentRepository.save(appointment);
    }

    public void createAppointmentForDoctor(Appointment appointment, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"DOCTOR".equals(user.getRole().name())) {
            throw new RuntimeException("Only doctors can create appointments");
        }
        //appointment.setId(9L);
        appointment.setDoctor(user.getDoctor());
        appointment.setStatus(AppointmentStatus.Open);

        appointmentRepository.save(appointment);
    }
}