package com.medicalregister.service;

import com.medicalregister.model.Patient;
import com.medicalregister.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existing = patientRepository.findById(id).orElseThrow();
        existing.setName(updatedPatient.getName());
        existing.setAge(updatedPatient.getAge());
        existing.setMedicalHistory(updatedPatient.getMedicalHistory());
        return patientRepository.save(existing);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}