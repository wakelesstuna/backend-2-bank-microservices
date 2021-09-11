package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AccountDto {
    private UUID id;
    private UUID holderId;
    private int accountNumber;
    private double balance;

    @JsonCreator
    public AccountDto(@JsonProperty("id")UUID id,
                      @JsonProperty("holderId") UUID holderId,
                      @JsonProperty("accountNumber") int accountNumber,
                      @JsonProperty("balance")double balance) {
        this.id = id;
        this.holderId = holderId;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
