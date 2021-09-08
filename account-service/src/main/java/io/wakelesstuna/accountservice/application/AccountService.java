package io.wakelesstuna.accountservice.application;

import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import io.wakelesstuna.accountservice.presentation.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Transactional
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountDto createAccountDto) {
        UUID holderId = createAccountDto.getHolderId();

        Account account = new Account(holderId);
        accountRepository.save(account);
        log.info("new account created for user: {}", holderId);
        return account;
    }

    public TransactionResponseDto deposit(TransactionDto transactionDto) {
        log.info("object {}", transactionDto);
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new NoSuchElementException("did not found account"));
        double amount = transactionDto.getAmount();

        account.deposit(amount);

        account = accountRepository.update(account);
        final String msg = String.format("deposit amount: %s to account: %s",amount, account.getId());
        log.info(msg);
        return new TransactionResponseDto(msg);
    }

    public TransactionResponseDto withdraw(TransactionDto transactionDto) {
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new NoSuchElementException("did not found account"));
        double amount = transactionDto.getAmount();

        boolean isAccepted = account.withdraw(amount);
        String msg;
        if (isAccepted) {
            account = accountRepository.update(account);
            msg = String.format("withdraw amount: %s to account: %s",amount, account.getId());
        } else {
            msg = "balance can't be less then 0";
        }

        log.info(msg);
        return new TransactionResponseDto(msg);
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        log.info("fetched all accounts");
        return accounts;
    }

    public int getTotalAccountsCountForUser(UUID holderId) {
        int totalAccounts = accountRepository.findAllByHolderId(holderId).size();
        log.info("fetched total accounts for user {}", holderId);
        return totalAccounts;
    }

    public AccountListDto getListOfUserAccounts(UUID holderId) {
        List<Account> accounts = accountRepository.findAllByHolderId(holderId);
        log.info("fetched all accounts for user {}", holderId);
        return convertToAccountListDto(accounts);
    }

    private AccountListDto convertToAccountListDto(List<Account> accounts) {
        List<AccountDto> collect = accounts.stream().map(account -> {
            UUID id = account.getId();
            UUID holderId = account.getHolderId();
            double balance = account.getBalance();
            return new AccountDto(id, holderId, balance);
        }).collect(Collectors.toList());

        return new AccountListDto(collect);
    }
}
