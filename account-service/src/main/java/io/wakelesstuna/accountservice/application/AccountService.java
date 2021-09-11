package io.wakelesstuna.accountservice.application;

import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.domain.AccountRepository;
import io.wakelesstuna.accountservice.presentation.dto.*;
import io.wakelesstuna.accountservice.risk.RiskApi;
import io.wakelesstuna.accountservice.risk.RiskAssignmentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final RiskApi riskApi;

    private final String riskApiBaseUrl;
    private final String assignmentEndPoint;

    public AccountService(AccountRepository accountRepository, RiskApi riskApi, String riskApiBaseUrl, String assignmentEndPoint) {
        this.accountRepository = accountRepository;
        this.riskApi = riskApi;
        this.riskApiBaseUrl = riskApiBaseUrl;
        this.assignmentEndPoint = assignmentEndPoint;
    }

    public Account createAccount(CreateAccountDto createAccountDto) {
        UUID holderId = createAccountDto.getHolderId();

        if (riskApi.callService(riskApiBaseUrl,assignmentEndPoint,holderId.toString(), RiskAssignmentDto.class).isPass()){
            List<Account> accounts = accountRepository.findAllByHolderId(holderId);

            int newAccountNumber = getHighestAccountNumber(accounts) + 1;

            Account account = new Account(holderId, newAccountNumber);
            accountRepository.save(account);

            log.info("new account created for user: {}, with account number: {}", holderId, newAccountNumber);
            return account;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "user did not pass the credit check");
        }
    }

    public TransactionResponseDto deposit(TransactionDto transactionDto) {
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

    public AccountListDto convertToAccountListDto(List<Account> accounts) {
        List<AccountDto> collect = accounts.stream().map(account -> {
            UUID id = account.getId();
            UUID holderId = account.getHolderId();
            int accountNumber = account.getAccountNumber();
            double balance = account.getBalance();
            return new AccountDto(id, holderId,accountNumber, balance);
        }).collect(Collectors.toList());

        return new AccountListDto(collect);
    }

    /**
     * Extracts the highest account number from a list of account numbers
     * @param accounts list of accounts
     * @return int that is the highest account number in the list
     */
    public int getHighestAccountNumber(List<Account> accounts) {
        return accounts.stream()
                .mapToInt(Account::getAccountNumber)
                .max()
                .orElse(0);
    }
}
