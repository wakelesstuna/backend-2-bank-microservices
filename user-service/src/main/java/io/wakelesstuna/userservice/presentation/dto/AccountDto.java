package io.wakelesstuna.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    private UUID holderId;
    private double balance;

    @JsonCreator
    public AccountDto(@JsonProperty("id")UUID id,@JsonProperty("holderId") UUID holderId, @JsonProperty("balance")double balance) {
        this.id = id;
        this.holderId = holderId;
        this.balance = balance;
    }
}
