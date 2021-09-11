package io.wakelesstuna.riskservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RiskServiceApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldFailRiskServiceForDan() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/risk/Dan"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.pass")
                        .value(false));
    }

    @Test
    void ShouldReturn404IfUrlIsWrong() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notValidUrl"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnTrueIfRiskAssessmentForDbn() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/risk/Dbn"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.pass")
                        .value(true));
    }
}
