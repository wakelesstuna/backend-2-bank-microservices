package io.wakelesstuna.userservice.security.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RoleDto {

    private final String name;

    @JsonCreator
    public RoleDto(@JsonProperty("name") String name) {
        this.name = name;
    }
}
