package io.wakelesstuna.userservice;

import io.wakelesstuna.userservice.domain.Role;
import io.wakelesstuna.userservice.domain.RoleRepository;
import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            Role admin1 = roleRepository.save(new Role("ADMIN"));
            log.info("Add role ADMIN");
            Role user1 = roleRepository.save(new Role("USER"));
            log.info("Add role USER");
            User admin = new User("admin", "letmein", Arrays.asList(admin1));
            userRepository.save(admin);
            log.info("Add admin to database");
            User user = new User("user", "letmein", Arrays.asList(user1));
            userRepository.save(user);
            log.info("Add user to database");
        };
    }*/

}
