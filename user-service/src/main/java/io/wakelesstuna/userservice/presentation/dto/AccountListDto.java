package io.wakelesstuna.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AccountListDto {

    List<AccountDto> accounts;

    @JsonCreator
    public AccountListDto(@JsonProperty("accounts")List<AccountDto> accounts) {
        this.accounts = accounts;
    }
}
