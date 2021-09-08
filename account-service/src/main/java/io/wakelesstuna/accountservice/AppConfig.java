package io.wakelesstuna.accountservice;

import io.wakelesstuna.accountservice.application.AccountService;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import io.wakelesstuna.accountservice.persistance.AccountRepositoryImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {

    @Bean
    public AccountRepository accountRepository(EntityManager entityManager){
        return new AccountRepositoryImp(entityManager);
    }

    @Bean
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountService(accountRepository);
    }

}
