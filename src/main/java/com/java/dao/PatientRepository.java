package com.java.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.dto.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
