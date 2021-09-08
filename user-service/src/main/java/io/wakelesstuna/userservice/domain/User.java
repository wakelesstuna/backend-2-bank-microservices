package io.wakelesstuna.userservice.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wakelesstuna.userservice.presentation.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    private String username;

    private String password;

    @OneToMany
    private List<Role> roles;

    @JsonCreator
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password,@JsonProperty("roles") List<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void encryptPasswordForUser(PasswordEncoder encoder){
        this.password = encoder.encode(this.password);
    }

    public void saveAllRolesForUser(RoleRepository roleRepository) {
        this.roles.forEach(roleRepository::save);
    }

    public UserDto toUserDto() {
        return new UserDto(this.id, this.username, 0);
    }
}
