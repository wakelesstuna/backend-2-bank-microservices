package io.wakelesstuna.riskservice.presentation;

public class RiskScoreDto {
    private final boolean pass;

    public RiskScoreDto(boolean pass) {
        this.pass = pass;
    }

    public static RiskScoreDto pass() {
        return new RiskScoreDto(true);
    }

    public static RiskScoreDto fail() {
        return new RiskScoreDto(false);
    }
}
