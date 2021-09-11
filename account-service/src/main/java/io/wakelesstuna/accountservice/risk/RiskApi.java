package io.wakelesstuna.accountservice.risk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.ConnectException;
import java.util.Objects;

@Slf4j
public class RiskApi {

    public <T> T callService(String baseUrl, String endPoint, String pathVariable, Class<T> t)  {

        final RestTemplate restTemplate = new RestTemplate();
        final String url = String.format("%s%s/%s",baseUrl,endPoint, pathVariable);
        ResponseEntity<T> entity;

        entity = exponentialBackoff(restTemplate, url, t);
        if(entity == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (entity.getStatusCodeValue() != 200){
            final String errorMsg = "didn't retrieve any object from risk api";
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,errorMsg);
        }
        return Objects.requireNonNull(entity.getBody());
    }

    public <T> ResponseEntity<T> exponentialBackoff(RestTemplate restTemplate, String url, Class<T> t) {
        int retries = 0;
        int baseDelay = 10;
        int jitter = 50;

        while (retries < 3) {
            try {
                return restTemplate.getForEntity(url, t);
            } catch (Exception e){
                int delay = (int) (Math.pow(baseDelay,retries) + jitter);
                final String errorMsg = String.format("trying GET request from %s again", url);
                log.error(errorMsg);
                waitToCallServiceAgain(Integer.toUnsignedLong(delay));
                retries += 1;
            }
        }
        return null;
    }

    private void waitToCallServiceAgain(Long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
