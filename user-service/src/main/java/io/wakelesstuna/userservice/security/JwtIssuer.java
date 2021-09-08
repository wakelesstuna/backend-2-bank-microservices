package io.wakelesstuna.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class JwtIssuer {

    private final Algorithm algorithm;
    private final Duration validity;

    public JwtIssuer(Algorithm algorithm, Duration validity) {
        this.validity = validity;
        this.algorithm = algorithm;
    }

    public String generateToken(final User user, HttpServletRequest request) {

        String jwtToken = JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plus(validity)))
                .withIssuer(request.getRequestURL().toString())
                // här sätter man role/authority i sin application
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        log.info("jwt {}", jwtToken);
        return jwtToken;
    }

    public DecodedJWT decodedJwt(String jwtToken) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwtToken);
    }

}
