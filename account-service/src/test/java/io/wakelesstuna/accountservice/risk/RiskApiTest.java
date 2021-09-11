package io.wakelesstuna.accountservice.risk;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import org.hibernate.jdbc.Expectations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class RiskApiTest {


    private WireMockServer riskMock;

    @Mock
    RestTemplate restTemplate;

    @Mock
    Thread thread;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        riskMock = new WireMockServer(7070);
        riskMock.start();
    }

    @AfterEach
    void tearDown() {
        riskMock.stop();
    }

    @Test
    void canMakeRiskAssignment() {
        riskMock.stubFor(get(urlEqualTo("/risk/2"))
                .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE,MimeTypeUtils.APPLICATION_JSON.toString())
                .withBody("{\"pass\": true}")));
        final RiskApi riskApi = new RiskApi();
        RiskAssignmentDto riskAssignmentDto = riskApi.callService(riskMock.baseUrl(), "/risk", "2", RiskAssignmentDto.class);
        assertTrue(riskAssignmentDto.isPass());
    }

    @Test
    void canMakeRiskAssignmentBackoff() {

        riskMock.stubFor(get(urlEqualTo("/risk/2"))
                .willReturn(aResponse()
                        .withFault(Fault.EMPTY_RESPONSE)));

        final RiskApi riskApi = new RiskApi();
        ResponseEntity<RiskAssignmentDto> entity = riskApi.exponentialBackoff(restTemplate, riskMock.baseUrl() + "/risk/2", RiskAssignmentDto.class);

        assertNull(entity);
    }
}