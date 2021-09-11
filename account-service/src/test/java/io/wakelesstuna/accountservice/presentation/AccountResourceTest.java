package io.wakelesstuna.accountservice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wakelesstuna.accountservice.application.AccountService;
import io.wakelesstuna.accountservice.containers.MyPostgresTestContainer;
import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import io.wakelesstuna.accountservice.presentation.dto.CreateAccountDto;
import io.wakelesstuna.accountservice.presentation.dto.TransactionDto;
import io.wakelesstuna.accountservice.presentation.dto.TransactionResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountResourceTest extends MyPostgresTestContainer {

    @MockBean
    AccountService accountService;

    @MockBean
    AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    private UUID holderId;
    private UUID accountId;
    private ObjectMapper mapper;

    @BeforeEach
    void init() {
        mapper = new ObjectMapper();
        holderId = UUID.fromString("246b9c95-4974-45ae-899c-5d30a4599fc6");
        accountId = UUID.fromString("856b9c95-4974-45ae-899c-5d30a4599fc6");
    }
    @Test
    void getAllAccountsTest() throws Exception {
        final String url = "/api/accounts";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getListOfUserAccountsTest() throws Exception {
        final String url = "/api/accounts/";
        mockMvc.perform(MockMvcRequestBuilders.get(url + holderId))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getTotalAccountsCount() throws Exception {
        final String url = "/api/accounts/total/accounts/";
        mockMvc.perform(MockMvcRequestBuilders.get(url + holderId))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createAccountTest() throws Exception {
        final String url = "/api/accounts/create";
        CreateAccountDto request = new CreateAccountDto(holderId);

        Account account = new Account(holderId, 1);

        when(accountService.createAccount(any(CreateAccountDto.class)))
                .thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.holderId")
                        .value(request.getHolderId().toString()));
    }

    @Test
    void depositTest() throws Exception {
        final String url = "/api/accounts/deposit";
        final double amount = 10;

        TransactionDto transactionDto = new TransactionDto(accountId,amount);

        String expected = String.format("deposit amount: %s to account: %s",amount,accountId);

        when(accountService.deposit(any(TransactionDto.class)))
                .thenReturn(new TransactionResponseDto(expected));

        mockMvc.perform(MockMvcRequestBuilders.post(url)
        .content(mapper.writeValueAsString(transactionDto))
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message")
                        .value(expected));

        verify(accountService, times(1))
                .deposit(any(TransactionDto.class));
    }

    @Test
    void withdraw() throws Exception {
        final String url = "/api/accounts/withdraw";
        final double amount = 10;

        TransactionDto transactionDto = new TransactionDto(accountId,amount);

        String expected = String.format("withdraw amount: %s to account: %s",amount, accountId);

        when(accountService.withdraw(any(TransactionDto.class)))
                .thenReturn(new TransactionResponseDto(expected));

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(mapper.writeValueAsString(transactionDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message")
                        .value(expected));

        verify(accountService, times(1))
                .withdraw(any(TransactionDto.class));
    }
}