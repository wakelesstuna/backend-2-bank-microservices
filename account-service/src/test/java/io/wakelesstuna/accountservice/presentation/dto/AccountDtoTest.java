package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountDtoTest {

    private UUID id = UUID.fromString("246b9c95-4974-45ae-899c-5d30a4599fc6");
    private UUID holderId = UUID.fromString("246b9c95-4974-45ae-899c-5d30a4599fc6");


    private String payload = "" +
            "{\n" +
            "    \"id\": \"246b9c95-4974-45ae-899c-5d30a4599fc6\",\n" +
            "    \"holderId\": \"246b9c95-4974-45ae-899c-5d30a4599fc6\",\n" +
            "    \"accountNumber\": 10,\n" +
            "    \"balance\": 0\n" +
            "}";

    @Test
    void canSerialize() throws JsonProcessingException {
        AccountDto accountDto = new ObjectMapper().readValue(payload, AccountDto.class);
        assertEquals(id,accountDto.getId());
        assertEquals(holderId,accountDto.getHolderId());
        assertEquals(10,accountDto.getAccountNumber());
        assertEquals(0,accountDto.getBalance());
    }

}