package io.wakelesstuna.userservice;

import com.auth0.jwt.algorithms.Algorithm;
import io.wakelesstuna.userservice.application.AccountService;
import io.wakelesstuna.userservice.application.UserService;
import io.wakelesstuna.userservice.domain.RoleRepository;
import io.wakelesstuna.userservice.domain.UserRepository;
import io.wakelesstuna.userservice.persistance.RoleRepositoryImp;
import io.wakelesstuna.userservice.persistance.UserRepositoryImp;
import io.wakelesstuna.userservice.security.JwtIssuer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${security.algorithmSecret}")
    private String algorithmSecret;
    @Value("${security.validMinutes}")
    private int validMinutes;
    @Value("${security.bCryptStrength}")
    private int bCryptStrength;
    @Value("${accountService.baseUrl}")
    private String accountServiceBaseUrl;

    @Bean
    public Algorithm algorithm(){
        return Algorithm.HMAC256(algorithmSecret.getBytes());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(bCryptStrength);
    }

    @Bean
    public JwtIssuer jwtIssuer(Algorithm algorithm) {
        return new JwtIssuer(algorithm, Duration.ofMinutes(validMinutes));
    }

    @Bean
    public RoleRepository roleRepository(EntityManager entityManager) {
        return new RoleRepositoryImp(entityManager);
    }

    @Bean
    public UserRepository userRepository(EntityManager entityManager) {
        return new UserRepositoryImp(entityManager);
    }

    @Bean
    public UserService userService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        return new UserService(userRepository,roleRepository, passwordEncoder, accountServiceBaseUrl);
    }

    @Bean
    public AccountService accountService(UserRepository userRepository) {
        return new AccountService(userRepository, accountServiceBaseUrl);
    }

}
