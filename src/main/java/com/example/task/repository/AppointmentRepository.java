package com.example.task.repository;

import com.example.task.model.Appointment;
import com.example.task.model.Doctor;
import com.example.task.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status);
    List<Appointment> findByDateTimeBetweenAndStatus(
            LocalDateTime start, LocalDateTime end, AppointmentStatus status);
    List<Appointment> findByDateTimeBetween(
            LocalDateTime start,
            LocalDateTime end);

    List<Appointment> findByDoctorIdAndDateTimeBetween(
            Long doctorId,
            LocalDateTime start,
            LocalDateTime end);
}