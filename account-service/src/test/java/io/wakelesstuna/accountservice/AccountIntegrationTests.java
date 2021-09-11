package io.wakelesstuna.accountservice;

import io.wakelesstuna.accountservice.containers.MyPostgresTestContainer;
import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// @Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountIntegrationTests extends MyPostgresTestContainer {

    @Autowired
    AccountRepository accountRepository;

    /*@Container
    private static PostgreSQLContainer container = new PostgreSQLContainer("postgres:alpine");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        // overriding the application.props file with the credentials from
        // the container we spin up
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
*/
    @Test
    @Order(1)
    void when_using_a_clean_db_this_should_return_empty() {
        List<Account> all = accountRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    @Order(2)
    void saveTest() {
        UUID holderId = UUID.randomUUID();
        Account account = new Account(holderId, 1);
        accountRepository.save(account);
        Optional<Account> byHolderId = accountRepository.findByHolderId(holderId);

        assertNotNull(byHolderId);
        assertEquals(holderId, byHolderId.get().getHolderId());
    }


}
