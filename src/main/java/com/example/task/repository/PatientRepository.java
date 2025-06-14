package com.example.task.repository;

import com.example.task.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    //Optional<Patient> findByEmail(String email); //jednak nie użyłem tego
}