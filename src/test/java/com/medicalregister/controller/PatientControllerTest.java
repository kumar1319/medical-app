package com.medicalregister.controller;

import com.medicalregister.model.Patient;
import com.medicalregister.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for PatientController
 */
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PatientService patientService;

    // A helper JWT post-processor that adds scope "read:patients"
    private static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor withReadScope() {
        return jwt().authorities(() -> "SCOPE_read:patients");
    }

    @Test
    @DisplayName("GET /api/patients — with valid scope returns list")
    void testGetAllPatients_withAuth() throws Exception {
        // Given
        List<Patient> mockList = List.of(
                new Patient(1L, "Alice", 30, "None"),
                new Patient(2L, "Bob",   40, "Asthma")
        );
        given(patientService.getAllPatients()).willReturn(mockList);

        // When & Then
        mvc.perform(get("/api/patients")
                        .with(withReadScope())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].medicalHistory").value("Asthma"));
    }

    @Test
    @DisplayName("GET /api/patients – no token → 401 Unauthorized")
    void testGetAllPatients_withoutToken() throws Exception {
        mvc.perform(get("/api/patients"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("POST /api/patients — with scope saves and returns patient")
    void testAddPatient_withAuth() throws Exception {
        Patient input = new Patient(null, "Carol", 25, "Healthy");
        Patient saved = new Patient(3L, "Carol", 25, "Healthy");

        given(patientService.savePatient(any(Patient.class))).willReturn(saved);

        mvc.perform(post("/api/patients")
                        .with(withReadScope())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Carol",
                      "age": 25,
                      "medicalHistory": "Healthy"
                    }
                    """
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Carol"));
    }

    @Test
    @DisplayName("PUT /api/patients/{id} — with scope updates and returns patient")
    void testUpdatePatient_withAuth() throws Exception {
        Patient updated = new Patient(1L, "AliceUpdated", 31, "None");
        given(patientService.updatePatient(eq(1L), any(Patient.class))).willReturn(updated);

        mvc.perform(put("/api/patients/1")
                        .with(withReadScope())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "AliceUpdated",
                      "age": 31,
                      "medicalHistory": "None"
                    }
                    """
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AliceUpdated"))
                .andExpect(jsonPath("$.age").value(31));
    }

    @Test
    @DisplayName("DELETE /api/patients/{id} — with scope returns 200")
    void testDeletePatient_withAuth() throws Exception {
        Mockito.doNothing().when(patientService).deletePatient(1L);

        mvc.perform(delete("/api/patients/1")
                        .with(withReadScope())
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/patients – no token → 401")
    void noTokenGives401() throws Exception {
        mvc.perform(get("/api/patients"))
                .andExpect(status().isUnauthorized());
    }



    @Test
    @DisplayName("GET /api/patients – token with read:patients → 200 + body")
    void happyPathGives200() throws Exception {
        given(patientService.getAllPatients())
                .willReturn(List.of(new Patient(1L,"Alice",30,"None")));

        mvc.perform(get("/api/patients")
                        .with(jwt()
                                .jwt(jwt -> jwt
                                        .claim("scope", "read:patients")
                                )
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }


}
