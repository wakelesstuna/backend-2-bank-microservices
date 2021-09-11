package io.wakelesstuna.accountservice.persistance;

import io.wakelesstuna.accountservice.containers.MyPostgresTestContainer;
import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRepositoryTest extends MyPostgresTestContainer {

    @Autowired
    AccountRepository accountRepository;

    UUID holderId = UUID.fromString("246b9c95-4974-45ae-899c-5d30a4599fc6");

    @Test
    @Order(1)
    void save() {
        Account account = accountRepository.save(new Account(holderId, 1));
        assertNotNull(account);
    }

    @Test
    @Order(2)
    void findAllByHolderId() {
        Account account = accountRepository.findByHolderId(holderId)
                .orElseThrow(() -> new NoSuchElementException(""));
        assertEquals(holderId, account.getHolderId());
    }

    @Test
    @Order(3)
    void update() {
        double expect = 10;
        Account account = accountRepository.findByHolderId(holderId)
                .orElseThrow(() -> new NoSuchElementException(""));
        account.setBalance(expect);
        Account update = accountRepository.update(account);
        assertEquals(expect, update.getBalance());
    }

    @Test
    @Order(4)
    void findAll() {
        int expected = 1;
        List<Account> all = accountRepository.findAll();
        assertEquals(expected, all.size());
    }


    @Test
    @Order(5)
    void findByHolderId() {
        int expected = 2;
        Account account = new Account(holderId, 10);
        accountRepository.save(account);
        List<Account> allByHolderId = accountRepository.findAllByHolderId(holderId);

        assertEquals(expected, allByHolderId.size());
    }

    @Test
    @Order(6)
    void finByIdTest() {
        List<Account> allByHolderId = accountRepository.findAllByHolderId(holderId);
        Account account = allByHolderId.get(0);
        UUID expected = account.getId();

        Account actual = accountRepository.findById(expected).orElseThrow(() -> new NoSuchElementException(""));

        assertEquals(expected, actual.getId());


    }


}