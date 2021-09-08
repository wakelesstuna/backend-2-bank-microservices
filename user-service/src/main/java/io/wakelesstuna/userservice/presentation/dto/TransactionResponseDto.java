package io.wakelesstuna.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionResponseDto {

    private String message;

    @JsonCreator
    public TransactionResponseDto(@JsonProperty("message") String message) {
        this.message = message;
    }
}
