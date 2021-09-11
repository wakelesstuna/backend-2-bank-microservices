package io.wakelesstuna.riskservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiskResource {

    @GetMapping("/risk/{userId}")
    public ResponseEntity<RiskScoreDto> openAccount(@PathVariable("userId") String userId) {
        final RiskScoreDto scoreDto;
        if (userId.hashCode() % 2 == 0){
            scoreDto = RiskScoreDto.pass();
        }else {
            scoreDto = RiskScoreDto.fail();
        }
        return ResponseEntity.ok(scoreDto);
    }

}
