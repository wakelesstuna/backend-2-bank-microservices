package io.wakelesstuna.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wakelesstuna.userservice.security.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(exposedHeaders = "Authorization")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtIssuer jwtIssuer;

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager, final JwtIssuer jwtIssuer) {
        this.authenticationManager = authenticationManager;
        this.jwtIssuer = jwtIssuer;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest req,
                                                final HttpServletResponse res) throws AuthenticationException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        log.info("username: {} password: {}", username, password);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                req.getParameter("username"),
                req.getParameter("password"));

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain filterChain,
                                            final Authentication auth) {
        User user = (User) auth.getPrincipal();

        String jwtToken = jwtIssuer.generateToken(user, request);

        response.addCookie(new Cookie("jwt_token", jwtToken));

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, jwtToken);

        // TODO: 2021-09-07 Skapa ett filter för att kolla cookie och ett för headern

        // TODO: 2021-09-07 Sök på BFF backend for frontend

        //LoginResponseDto loginResponseDto = new LoginResponseDto(user.getUsername(), user.getAuthorities(),"Bearer " + jwtToken);


        LoginResponseDto loginResponseDto = new LoginResponseDto(user.getUsername(), LoginResponseDto.convertSimpleGrantedAuthority(user.getAuthorities()),"Bearer " + jwtToken);

        response.setContentType(MimeTypeUtils.APPLICATION_JSON.getType());
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), loginResponseDto);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        /*response.setHeader("Authorization", "Bearer " + jwtToken);
        String role = new ArrayList<>(user.getAuthorities()).get(0).getAuthority();
        Map<String, String> body = Map.of("username", user.getUsername(), "role", role, "jwt_token", jwtToken);

        writeMessageToResponseBody(body, response, MimeTypeUtils.APPLICATION_JSON);*/
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse response, AuthenticationException failed) {
        log.error("Authentication failed");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        Map<String, String> body = Map.of("error", "Authentication failed", "message", "wrong email or username");
        writeMessageToResponseBody(body, response, MimeTypeUtils.APPLICATION_JSON);
    }

    private void writeMessageToResponseBody(Map<String, String> body, HttpServletResponse response, MimeType contentType) {
        response.setContentType(contentType.getType());
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



}