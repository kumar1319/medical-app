package com.medicalregister.model;

import jakarta.persistence.*;

@Entity
public class Patient {
    public Patient() {
    }
    public Patient(Long id, String name, int age, String medicalHistory) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.medicalHistory = medicalHistory;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String medicalHistory;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
}