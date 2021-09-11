package io.wakelesstuna.userservice.application;

import io.wakelesstuna.userservice.domain.RoleRepository;
import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.domain.UserRepository;
import io.wakelesstuna.userservice.presentation.dto.AccountListDto;
import io.wakelesstuna.userservice.presentation.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    private final String accountServiceBaseUrl;

    private final String endPointCountForUserAccount = "/api/accounts/total/accounts/";

    private final String endPointForUserAccounts = "/api/accounts";

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, String accountServiceBaseUrl) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountServiceBaseUrl = accountServiceBaseUrl;
    }

    /**
     * Creates a new user, saving all roles to the database,
     * encrypting the password, then persist the user to the
     * database.
     * @param user the user you want to persist into the database
     * @return the persisted user
     */
    public UserDto createUser(User user) {
        user.saveAllRolesForUser(roleRepository);
        user.encryptPasswordForUser(passwordEncoder);
        User createdUser = userRepository.save(user);
        log.info("created user {}", createdUser.getUsername());
        return createdUser.toUserDto();
    }

    public UserDto getUserByUsername(String username) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));

        final String pathVariable = String.format("/%s", foundUser.getId());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AccountListDto> entity = restTemplate.getForEntity(accountServiceBaseUrl + endPointForUserAccounts + pathVariable, AccountListDto.class);
        if (entity != null){
            return new UserDto(foundUser.getId(), foundUser.getUsername(), entity.getBody().getAccounts());
        }else {
            return  new UserDto(foundUser.getId(), foundUser.getUsername(), new ArrayList<>());
        }
    }

    /**
     * Fetching all users from the database, then makes a call to
     * the account service to get number of accounts for a user back,
     * then creates a new list with UserDto objects
     * @return list of UserDto objects
     */
    public List<UserDto> getAllUsers() {
        List<User> all = userRepository.findAll();
        RestTemplate restTemplate = new RestTemplate();

        return all.stream().map(user -> {
            UUID id = user.getId();
            String username = user.getUsername();
            int numberOfAccounts;
            try {
                numberOfAccounts = restTemplate.getForEntity(accountServiceBaseUrl + endPointCountForUserAccount + id, Integer.class).getBody();
            } catch (NullPointerException e) {
                numberOfAccounts = 0;
            }
            return new UserDto(id, username, numberOfAccounts);
        }).collect(Collectors.toList());
    }

    /**
     * Tells spring security how to fetch the user
     * @param username the username of the user
     * @return UserDetails object for spring security to use
     * @throws UsernameNotFoundException if no user is found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            final String errorMessage = String.format("User not found in the database: %S", username);
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        } else {
            log.info("User found in the database: {}",  username);
        }
        User user = userOpt.get();
        Collection<SimpleGrantedAuthority> authorities = user.getAuthorities();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


}
