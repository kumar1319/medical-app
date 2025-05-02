package com.medicalregister.config;

import com.medicalregister.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class) // Import the SecurityConfig
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService; // Mock the PatientService bean

    @Test
    void testUnauthorizedAccessToSecureEndpoint() throws Exception {
        mockMvc.perform(get("/secure-endpoint"))
                .andExpect(status().isUnauthorized());
    }
}