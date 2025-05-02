package com.medicalregister.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void testNoArgsConstructor() {
        // Test the no-argument constructor
        Patient patient = new Patient();
        assertNull(patient.getId());
        assertNull(patient.getName());
        assertNull(patient.getAge());
        assertNull(patient.getMedicalHistory());
    }

    @Test
    void testAllArgsConstructor() {
        // Test the parameterized constructor
        Patient patient = new Patient(1L, "John Doe", 30, "Diabetes");
        assertEquals(1L, patient.getId());
        assertEquals("John Doe", patient.getName());
        assertEquals(30, patient.getAge());
        assertEquals("Diabetes", patient.getMedicalHistory());
    }

    @Test
    void testSettersAndGetters() {
        // Test the setters and getters
        Patient patient = new Patient();

        patient.setId(2L);
        assertEquals(2L, patient.getId());

        patient.setName("Jane Smith");
        assertEquals("Jane Smith", patient.getName());

        patient.setAge(25);
        assertEquals(25, patient.getAge());

        patient.setMedicalHistory("Asthma");
        assertEquals("Asthma", patient.getMedicalHistory());
    }
}