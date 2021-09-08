package io.wakelesstuna.userservice.presentation;

import io.wakelesstuna.userservice.application.AccountService;
import io.wakelesstuna.userservice.application.UserService;
import io.wakelesstuna.userservice.domain.Role;
import io.wakelesstuna.userservice.domain.RoleRepository;
import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.presentation.dto.TransactionDto;
import io.wakelesstuna.userservice.presentation.dto.TransactionResponseDto;
import io.wakelesstuna.userservice.presentation.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(exposedHeaders = "authorization")
@RequestMapping("/api/users")
public class UserResource {

    private final UserService userService;

    private final AccountService accountService;

    private final RoleRepository roleRepository;

    @Autowired
    public UserResource(UserService userService, AccountService accountService, RoleRepository roleRepository) {
        this.userService = userService;
        this.accountService = accountService;
        this.roleRepository = roleRepository;
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
     * Deposit a specific amount to an account
     * @param transactionDto an object that contains account id and the amount to deposit
     * @return
     */
    @PutMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(@RequestBody TransactionDto transactionDto) {
        ResponseEntity<TransactionResponseDto> entity = accountService.depositToAccount(transactionDto);
        return ResponseEntity.status(entity.getStatusCode()).body(entity.getBody());
    }

    /**
     * Withdraw a specific amount to an account
     * @param transactionDto an object that contains account id and the amount to Withdraw
     * @return
     */
    @PutMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(@RequestBody TransactionDto transactionDto) {
        ResponseEntity<TransactionResponseDto> stringResponseEntity = accountService.withdrawFromAccount(transactionDto);
        return ResponseEntity.ok().body(stringResponseEntity.getBody());
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }
}
