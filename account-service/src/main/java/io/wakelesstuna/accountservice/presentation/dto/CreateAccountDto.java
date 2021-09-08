package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAccountDto {
    private UUID holderId;

    @JsonCreator
    public CreateAccountDto(@JsonProperty("holderId") UUID holderId) {
        this.holderId = holderId;
    }
}
