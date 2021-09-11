package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateAccountDtoTest {

    private UUID holderId = UUID.fromString("246b9c95-4974-45ae-899c-5d30a4599fc6");


    private String payload = "" +
            "{\n" +
            "    \"holderId\": \"246b9c95-4974-45ae-899c-5d30a4599fc6\"\n" +
            "}";

    @Test
    void canSerialize() throws JsonProcessingException {
        CreateAccountDto createAccountDto = new ObjectMapper().readValue(payload, CreateAccountDto.class);
        assertEquals(holderId,createAccountDto.getHolderId());
    }

}