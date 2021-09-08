package io.wakelesstuna.userservice.security.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDtoTest {

    final String jsonBlob = "" +
            "{\n" +
            "    \"name\": \"ADMIN\"\n" +
            "}";

    @Test
    void canSerialize() throws JsonProcessingException {
        RoleDto roleDto = new ObjectMapper().readValue(jsonBlob,RoleDto.class);

        assertEquals("ADMIN", roleDto.getName());
    }
}