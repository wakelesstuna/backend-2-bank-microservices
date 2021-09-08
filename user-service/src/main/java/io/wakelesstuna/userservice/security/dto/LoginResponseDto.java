package io.wakelesstuna.userservice.security.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LoginResponseDto {
    @JsonProperty("username")
    private final String username;
    @JsonProperty("roles")
    private final List<RoleDto> roles;
    @JsonProperty("jwt_token")
    private final String jwtToken;

    @JsonCreator
    public LoginResponseDto(@JsonProperty("username") String username,@JsonProperty("roles") List<RoleDto> roles,@JsonProperty("jwt_token") String jwtToken) {
        this.username = username;
        // this.roles = convertSimpleGrantedAuthority(roles);
        this.roles = roles;
        this.jwtToken = jwtToken;
    }

    public static List<RoleDto> convertSimpleGrantedAuthority(Collection<GrantedAuthority> roles) {
        return roles.stream()
                .map(role -> new RoleDto(role.getAuthority()))
                .collect(Collectors.toList());
    }
}
