package io.wakelesstuna.userservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class CreateAccountDto {
    private UUID holderId;
    private String username;
    private String password;

    @JsonCreator
    public CreateAccountDto(@JsonProperty("holderId") UUID holderId,@JsonProperty("username") String username,@JsonProperty("password") String password) {
        this.holderId = holderId;
        this.username = username;
        this.password = password;
    }
}
