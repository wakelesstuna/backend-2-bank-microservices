package io.wakelesstuna.userservice.application;

import io.wakelesstuna.userservice.presentation.dto.*;
import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;

@Slf4j
@Transactional
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String accountServiceBaseUrl;
    private String getAllAccountsEndPoint = "/api/accounts";
    private String getAllAccountsOfUser = "/api/accounts";
    private String getTotalAccountsCountForUser = "/api/accounts/total/accounts/{holderId}";
    private String createAccountEndPoint = "/api/accounts/create";
    private final String depositEndPoint = "/api/accounts/deposit";
    private final String withdrawEndPoint = "/api/accounts/withdraw";

    public AccountService(UserRepository userRepository, PasswordEncoder passwordEncoder, String accountServiceBaseUrl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public AccountDto createAccount(CreateAccountDto createAccountDto) {
        User foundUser = userRepository.findByUsername(createAccountDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("no user found"));
        String password = createAccountDto.getPassword();

        boolean matches = passwordEncoder.matches(password, foundUser.getPassword());

        log.info("password matches ? {}", matches);

        if (matches) {
            createAccountDto = new CreateAccountDto(foundUser.getId(), null, null);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<AccountDto> entity;
            try {
                 entity = restTemplate.postForEntity(accountServiceBaseUrl + createAccountEndPoint, createAccountDto, AccountDto.class);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,ex.getMessage());
            }

            return entity.getBody();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password does not match");
        }
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
