package io.wakelesstuna.accountservice.application;

import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import io.wakelesstuna.accountservice.presentation.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private AccountService accountService;
    private List<Account> accounts;
    private UUID uuid;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void init() {
        accountService = new AccountService(accountRepository);
        accounts = List.of(new Account(UUID.randomUUID(),8),
                new Account(UUID.randomUUID(),5),
                new Account(UUID.randomUUID(),3),
                new Account(UUID.randomUUID(),4),
                new Account(UUID.randomUUID(),2));
        uuid = UUID.fromString("246b9c95-4974-45ae-899c-5d30a4599fc6");
    }

    @Test
    void createAccountTest() {


        when(accountRepository.findAllByHolderId(uuid))
                .thenReturn(accounts);

        when(accountRepository.save(any(Account.class)))
                .thenReturn(new Account());

        CreateAccountDto createAccountDto = new CreateAccountDto(uuid);

        Account account = accountService.createAccount(createAccountDto);

        assertEquals(uuid, account.getHolderId());

        verify(accountRepository, timeout(1)).findAllByHolderId(any(UUID.class));
        verify(accountRepository, timeout(1)).save(any(Account.class));
    }


    @Test
    void getHighestAccountNumberTest() {
        int expected = 8;
        int actual = accountService.getHighestAccountNumber(accounts);

        assertEquals(expected, actual);
    }

    @Test
    void getHighestAccountNumberNoAccountsTest() {
        List<Account> accounts = new ArrayList<>();
        int expected = 0;
        int actual = accountService.getHighestAccountNumber(accounts);

        assertEquals(expected, actual);
    }


    @Test
    void convertToAccountListDtoTest() {
        List<AccountDto> accountDtos = List.of(
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),8,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),5,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),3,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),4,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),2,0));

        AccountListDto expected = new AccountListDto(accountDtos);
        AccountListDto actual = accountService.convertToAccountListDto(accounts);

        assertEquals(expected.getAccounts().get(0).getClass(), actual.getAccounts().get(0).getClass());
        assertEquals(expected.getAccounts().size(), actual.getAccounts().size());
    }


    @Test
    void depositTest() {
        double amount = 10;
        TransactionDto transactionDto = new TransactionDto(uuid,amount);

        Account account = new Account();
        account.setId(uuid);

        Account updatedAccount = new Account();
        updatedAccount.setId(uuid);
        updatedAccount.setBalance(10);

        final String msg = String.format("deposit amount: %s to account: %s", amount, account.getId());
        TransactionResponseDto expected = new TransactionResponseDto(msg);

        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(java.util.Optional.of(account));

        when(accountRepository.update(any(Account.class)))
                .thenReturn(updatedAccount);

        TransactionResponseDto actual = accountService.deposit(transactionDto);

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(accountRepository, times(1)).findById(any(UUID.class));
        verify(accountRepository, times(1)).update(any(Account.class));
    }

    @Test
    void withdrawTest() {
        double amount = 10;
        TransactionDto transactionDto = new TransactionDto(uuid,amount);

        Account account = new Account();
        account.setId(uuid);
        account.setBalance(20);

        Account updatedAccount = new Account();
        updatedAccount.setId(uuid);
        updatedAccount.setBalance(10);

        final String msg = String.format("withdraw amount: %s to account: %s",amount, account.getId());
        TransactionResponseDto expected = new TransactionResponseDto(msg);

        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(java.util.Optional.of(account));

        when(accountRepository.update(any(Account.class)))
                .thenReturn(updatedAccount);

        TransactionResponseDto actual = accountService.withdraw(transactionDto);

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(accountRepository, times(1)).findById(any(UUID.class));
        verify(accountRepository, times(1)).update(any(Account.class));
    }

    @Test
    void withdrawTestThrows() {
        double amount = 10;
        TransactionDto transactionDto = new TransactionDto(uuid,amount);

        Account account = new Account();
        account.setId(uuid);
        account.setBalance(0);

        Account updatedAccount = new Account();
        updatedAccount.setId(uuid);
        updatedAccount.setBalance(10);

        final String msg = "balance can't be less then 0";
        TransactionResponseDto expected = new TransactionResponseDto(msg);

        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(java.util.Optional.of(account));

        TransactionResponseDto actual = accountService.withdraw(transactionDto);

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(accountRepository, times(1)).findById(any(UUID.class));
        verify(accountRepository, times(0)).update(any(Account.class));
    }

    @Test
    void getAllAccountsTest() {
        int expected = accounts.size();

        when(accountRepository.findAll())
                .thenReturn(accounts);

        List<Account> actual = accountService.getAllAccounts();

        assertEquals(expected, actual.size());

        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void getTotalAccountsCountForUserTest() {
        int expected = accounts.size();

        when(accountRepository.findAllByHolderId(any(UUID.class)))
                .thenReturn(accounts);

        int actual = accountService.getTotalAccountsCountForUser(uuid);

        assertEquals(expected, actual);

        verify(accountRepository, times(1)).findAllByHolderId(any(UUID.class));
    }

    @Test
    void getListOfUserAccountsTest() {
        List<AccountDto> accountDtos = List.of(
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),8,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),5,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),3,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),4,0),
                new AccountDto(UUID.randomUUID(),UUID.randomUUID(),2,0));

        AccountListDto expected = new AccountListDto(accountDtos);

        when(accountRepository.findAllByHolderId(any(UUID.class)))
                .thenReturn(accounts);

        AccountListDto actual = accountService.getListOfUserAccounts(uuid);

        assertEquals(expected.getAccounts().size(), actual.getAccounts().size());

        verify(accountRepository, times(1)).findAllByHolderId(any(UUID.class));
    }
}