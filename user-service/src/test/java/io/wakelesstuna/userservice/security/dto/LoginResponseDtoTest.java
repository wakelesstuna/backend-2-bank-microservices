package io.wakelesstuna.userservice.security.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDtoTest {

    final String jsonBlob = "" +
            "{\n" +
            "    \"username\": \"user\",\n" +
            "    \"roles\": [\n" +
            "        {\n" +
            "            \"name\": \"ADMIN\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"jwt_token\": \"jwt\"\n" +
            "}";

    @Test
    void canSerialize() throws JsonProcessingException {
        final String expected = "jwt";
        LoginResponseDto loginResponseDto = new ObjectMapper().readValue(jsonBlob,LoginResponseDto.class);

        assertEquals(expected, loginResponseDto.getJwtToken());
    }

}