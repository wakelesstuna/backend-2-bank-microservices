package io.wakelesstuna.accountservice.presentation;

import io.wakelesstuna.accountservice.application.AccountService;
import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.presentation.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
public class AccountResource {

    @Autowired
    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();
        return ResponseEntity.ok().body(allAccounts);
    }

    @GetMapping("/{holderId}")
    public ResponseEntity<AccountListDto> getListOfUserAccounts(@PathVariable(name = "holderId") UUID holderId) {
        AccountListDto accounts = accountService.getListOfUserAccounts(holderId);
        log.info("list {}", accounts);
        return ResponseEntity.ok().body(accounts);
    }

    @GetMapping("/total/accounts/{holderId}")
    public ResponseEntity<Integer> getTotalAccountsCount(@PathVariable(name = "holderId") UUID holderId) {
        int totalAccounts = accountService.getTotalAccountsCountForUser(holderId);
        return ResponseEntity.ok().body(totalAccounts);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountDto createAccountDto) {
        Account account = accountService.createAccount(createAccountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(@RequestBody TransactionDto transactionDto) {
        log.info("account controller {}", transactionDto);
        TransactionResponseDto account = accountService.deposit(transactionDto);
        return ResponseEntity.ok().body(account);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(@RequestBody TransactionDto transactionDto) {
        TransactionResponseDto account = accountService.withdraw(transactionDto);
        return ResponseEntity.ok().body(account);
    }




}
