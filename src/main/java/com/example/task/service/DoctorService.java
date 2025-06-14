package com.example.task.service;

import com.example.task.model.Doctor;
import com.example.task.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor save(Doctor doctor) {
        doctorRepository.save(doctor);
        return doctor;
    }
}