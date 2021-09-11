package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Getter
public class TransactionResponseDto {

    private String message;

    @JsonCreator
    public TransactionResponseDto(@JsonProperty("message") String message) {
        this.message = message;
    }
}
