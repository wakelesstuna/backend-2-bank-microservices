package io.wakelesstuna.accountservice.risk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RiskAssignmentDto {

    private boolean pass;

    @JsonCreator
    public RiskAssignmentDto(@JsonProperty("isPass") boolean pass) {
        this.pass = pass;
    }

    public boolean isPass() {
        return pass;
    }
}
