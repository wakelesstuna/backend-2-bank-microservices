package io.wakelesstuna.accountservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class AccountListDto {

    List<AccountDto> accounts;

    @JsonCreator
    public AccountListDto(@JsonProperty("accounts") List<AccountDto> accounts) {
        this.accounts = accounts;
    }

}
