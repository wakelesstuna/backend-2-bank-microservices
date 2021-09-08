package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionDto {

    private UUID accountId;
    private double amount;

    @JsonCreator
    public TransactionDto(@JsonProperty("accountId")UUID accountId,@JsonProperty("amount")double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
}
