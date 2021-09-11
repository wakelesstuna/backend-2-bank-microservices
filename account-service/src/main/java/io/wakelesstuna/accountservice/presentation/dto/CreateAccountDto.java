package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateAccountDto {
    private UUID holderId;

    @JsonCreator
    public CreateAccountDto(@JsonProperty("holderId") UUID holderId) {
        this.holderId = holderId;
    }
}
