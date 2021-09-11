package io.wakelesstuna.userservice.presentation;

import io.wakelesstuna.userservice.application.AccountService;
import io.wakelesstuna.userservice.application.UserService;
import io.wakelesstuna.userservice.presentation.dto.AccountDto;
import io.wakelesstuna.userservice.presentation.dto.AccountListDto;
import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.presentation.dto.CreateAccountDto;
import io.wakelesstuna.userservice.presentation.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(exposedHeaders = "authorization")
@RequestMapping("/api/admin")
public class AdminResource {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AdminResource(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    /**
     * Fetching all users
     * @return a list of UserDto objects
     */
    @GetMapping("/get/all/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Fetching all account for a specific user
     * @param username for the user you wanna fetch the accounts for
     * @return AccountListDto object, a list of UserDto
     */
    @GetMapping("/all/accounts")
    public ResponseEntity<AccountListDto> getAllAccountsForUser(@RequestParam String username) {
        AccountListDto accounts = accountService.getAllAccountsForUser(username);
        return ResponseEntity.ok().body(accounts);
    }

    /**
     * Creates a user
     * @param user the user you wanna create
     * @return UserDto object
     */
    @PostMapping("/create/user")
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        UserDto createdUser = userService.createUser(user);
        return ResponseEntity.status(CREATED).body(createdUser);
    }

    /**
     * Creates an account for a user
     * @param createAccountDto information about the account you want to create
     * @return AccountDto object
     */
    @PostMapping("/account/open")
    public ResponseEntity<AccountDto> openAccount(@RequestBody CreateAccountDto createAccountDto) {
        AccountDto accountDto = accountService.createAccount(createAccountDto);
        return ResponseEntity.ok().body(accountDto);
    }

}
