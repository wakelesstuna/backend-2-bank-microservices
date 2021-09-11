package io.wakelesstuna.accountservice.risk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wakelesstuna.accountservice.presentation.dto.AccountDto;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RiskAssignmentDtoTest {

    private String payload = "" +
            "{\n" +
            "    \"pass\": true\n" +
            "}";

    @Test
    void canSerialize() throws JsonProcessingException {
        RiskAssignmentDto riskAssignmentDto = new ObjectMapper().readValue(payload, RiskAssignmentDto.class);
        assertTrue(riskAssignmentDto.isPass());
    }
}