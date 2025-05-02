package com.medicalregister.service;

import com.medicalregister.model.Patient;
import com.medicalregister.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    private PatientRepository patientRepository;
    private PatientService patientService;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        patientService = new PatientService();
        // Use reflection to inject the mocked repository
        patientService.patientRepository = patientRepository;

        patient1 = new Patient(1L, "John Doe", 30, "Diabetes");
        patient2 = new Patient(2L, "Jane Smith", 25, "Asthma");
    }

    @Test
    void testGetAllPatients() {
        // Arrange
        List<Patient> patients = Arrays.asList(patient1, patient2);
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> result = patientService.getAllPatients();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testSavePatient() {
        // Arrange
        Patient newPatient = new Patient(null, "Alice Brown", 40, "Hypertension");
        Patient savedPatient = new Patient(3L, "Alice Brown", 40, "Hypertension");
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        // Act
        Patient result = patientService.savePatient(newPatient);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Alice Brown", result.getName());
        verify(patientRepository, times(1)).save(newPatient);
    }

    @Test
    void testUpdatePatient() {
        // Arrange
        Patient updatedPatient = new Patient(null, "Bob White", 31, "Updated History");
        when(patientRepository.findById(eq(1L))).thenReturn(Optional.of(patient1));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient1);

        // Act
        Patient result = patientService.updatePatient(1L, updatedPatient);

        // Assert
        assertEquals("Bob White", result.getName());
        assertEquals(31, result.getAge());
        assertEquals("Updated History", result.getMedicalHistory());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(patient1);
    }

    @Test
    void testDeletePatient() {
        // Arrange
        doNothing().when(patientRepository).deleteById(1L);

        // Act
        patientService.deletePatient(1L);

        // Assert
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdatePatientThrowsExceptionWhenNotFound() {
        // Arrange
        Patient updatedPatient = new Patient(null, "Nonexistent Patient", 50, "Unknown");
        when(patientRepository.findById(eq(99L))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> patientService.updatePatient(99L, updatedPatient));
        verify(patientRepository, times(1)).findById(99L);
        verify(patientRepository, never()).save(any(Patient.class));
    }
}