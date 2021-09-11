package io.wakelesstuna.accountservice;

import io.wakelesstuna.accountservice.application.AccountService;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import io.wakelesstuna.accountservice.persistance.AccountRepositoryImp;
import io.wakelesstuna.accountservice.risk.RiskApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {

    @Value("${risk.base-url}")
    private String riskBaseUrl;
    @Value("${risk.pass.end-point}")
    private String assignmentEndPoint;

    @Bean
    public AccountRepository accountRepository(EntityManager entityManager){
        return new AccountRepositoryImp(entityManager);
    }

    @Bean
    public RiskApi riskApi() {
        return new RiskApi();
    }

    @Bean
    public AccountService accountService(AccountRepository accountRepository, RiskApi riskApi) {
        return new AccountService(accountRepository, riskApi,riskBaseUrl,assignmentEndPoint);
    }

}
