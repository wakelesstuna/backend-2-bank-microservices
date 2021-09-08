package io.wakelesstuna.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionDto {

    private UUID accountId;
    private double amount;

    @JsonCreator
    public TransactionDto(@JsonProperty("account_id") UUID accountId,@JsonProperty("amount") double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
}
