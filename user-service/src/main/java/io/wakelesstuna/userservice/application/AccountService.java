package io.wakelesstuna.userservice.application;

import io.wakelesstuna.userservice.presentation.dto.*;
import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Slf4j
@Transactional
public class AccountService {

    private final UserRepository userRepository;

    private final String accountServiceBaseUrl;
    private String getAllAccountsEndPoint = "/api/accounts";
    private String getAllAccountsOfUser = "/api/accounts";
    private String getTotalAccountsCountForUser = "/api/accounts/total/accounts/{holderId}";
    private String createAccountEndPoint = "/api/accounts/create";
    private final String depositEndPoint = "/api/accounts/deposit";
    private final String withdrawEndPoint = "/api/accounts/withdraw";

    public AccountService(UserRepository userRepository, String accountServiceBaseUrl) {
        this.userRepository = userRepository;
        this.accountServiceBaseUrl = accountServiceBaseUrl;
    }

    /**
     * Fetching all accounts for a user
     * @return
     */
    public AccountListDto getAllAccountsForUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("no user found"));

        String pathVariable = String.format("/%s", user.getId());

        RestTemplate restTemplate = new RestTemplate();
        AccountListDto accountListDto = restTemplate.getForObject(accountServiceBaseUrl + getAllAccountsOfUser + pathVariable, AccountListDto.class);
        log.info("userservice: {}", accountListDto);
        return accountListDto;
    }

    public AccountDto createAccount(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("no user found"));

        CreateAccountDto createAccountDto = new CreateAccountDto(foundUser.getId());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AccountDto> entity = restTemplate.postForEntity(accountServiceBaseUrl + createAccountEndPoint, createAccountDto, AccountDto.class);
        AccountDto body = entity.getBody();

        return body;
    }

    public ResponseEntity<TransactionResponseDto> depositToAccount(TransactionDto transactionDto) {
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<TransactionResponseDto> entity = restTemplate.postForEntity(accountServiceBaseUrl + depositEndPoint, transactionDto, TransactionResponseDto.class);

        return entity;

    }

    public ResponseEntity<TransactionResponseDto> withdrawFromAccount(TransactionDto transactionDto) {
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<TransactionResponseDto> entity = restTemplate.postForEntity(accountServiceBaseUrl + withdrawEndPoint, transactionDto, TransactionResponseDto.class);

        return entity;
    }


}
