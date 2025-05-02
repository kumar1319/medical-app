package com.medicalregister.repository;

import com.medicalregister.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Loads only JPA components for testing
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void testSavePatient() {
        // Arrange
        Patient patient = new Patient(null, "John Doe", 30, "Diabetes");

        // Act
        Patient savedPatient = patientRepository.save(patient);

        // Assert
        assertThat(savedPatient.getId()).isNotNull();
        assertThat(savedPatient.getName()).isEqualTo("John Doe");
        assertThat(savedPatient.getAge()).isEqualTo(30);
        assertThat(savedPatient.getMedicalHistory()).isEqualTo("Diabetes");
    }

    @Test
    void testFindPatientById() {
        // Arrange
        Patient patient = new Patient(null, "Jane Smith", 25, "Asthma");
        Patient savedPatient = patientRepository.save(patient);

        // Act
        Optional<Patient> foundPatient = patientRepository.findById(savedPatient.getId());

        // Assert
        assertThat(foundPatient).isPresent();
        assertThat(foundPatient.get().getName()).isEqualTo("Jane Smith");
    }

    @Test
    void testDeletePatient() {
        // Arrange
        Patient patient = new Patient(null, "Alice Brown", 40, "Hypertension");
        Patient savedPatient = patientRepository.save(patient);

        // Act
        patientRepository.deleteById(savedPatient.getId());
        Optional<Patient> deletedPatient = patientRepository.findById(savedPatient.getId());

        // Assert
        assertThat(deletedPatient).isNotPresent();
    }

    @Test
    void testUpdatePatient() {
        // Arrange
        Patient patient = new Patient(null, "Bob White", 50, "Healthy");
        Patient savedPatient = patientRepository.save(patient);

        // Act
        savedPatient.setAge(51);
        savedPatient.setMedicalHistory("Updated Medical History");
        Patient updatedPatient = patientRepository.save(savedPatient);

        // Assert
        assertThat(updatedPatient.getAge()).isEqualTo(51);
        assertThat(updatedPatient.getMedicalHistory()).isEqualTo("Updated Medical History");
    }

    @Test
    void testFindAllPatients() {
        // Arrange
        Patient patient1 = new Patient(null, "John Doe", 30, "Diabetes");
        Patient patient2 = new Patient(null, "Jane Smith", 25, "Asthma");
        patientRepository.save(patient1);
        patientRepository.save(patient2);

        // Act
        Iterable<Patient> patients = patientRepository.findAll();

        // Assert
        assertThat(patients).hasSize(2);
    }
}