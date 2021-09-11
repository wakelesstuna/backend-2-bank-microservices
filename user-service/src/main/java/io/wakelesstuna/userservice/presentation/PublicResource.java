package io.wakelesstuna.userservice.presentation;

import io.wakelesstuna.userservice.application.UserService;
import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.presentation.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/public")
public class PublicResource {

    private final UserService userService;

    @Autowired
    public PublicResource(UserService userService) {
        this.userService = userService;
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


}
