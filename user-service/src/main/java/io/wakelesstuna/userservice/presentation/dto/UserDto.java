package io.wakelesstuna.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private UUID id;
    private String username;
    private int numberOfAccounts;
    private List<AccountDto> accounts;

    @JsonCreator
    public UserDto(@JsonProperty("id")UUID id, @JsonProperty("username")String username, @JsonProperty("number_of_accounts") int numberOfAccounts) {
        this.id = id;
        this.username = username;
        this.numberOfAccounts = numberOfAccounts;
    }

    public UserDto(UUID id, String username, List<AccountDto> accounts) {
        this.id = id;
        this.username = username;
        this.accounts = accounts;
    }
}
