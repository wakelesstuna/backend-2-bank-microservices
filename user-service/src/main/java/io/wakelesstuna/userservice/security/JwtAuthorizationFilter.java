package io.wakelesstuna.userservice.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtIssuer jwtIssuer;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtIssuer jwtIssuer) {
        super(authManager);
        this.jwtIssuer = jwtIssuer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

       final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
       log.info("doing filter in JwtAuthorizationFilter... header {}", authorizationHeader);

        String key = request.getParameter("key");
        log.info("key: {}", key);
        Cookie[] cookies = request.getCookies();
        log.info("cookies: {}", (Object) cookies);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
              String jwtToken = authorizationHeader.substring("Bearer ".length());
              UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(jwtToken);
              log.info("user Authorization !");
              SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(String jwtToken) {
        DecodedJWT decodedJWT = jwtIssuer.decodedJwt(jwtToken);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = getAuthorities(roles);

        return new UsernamePasswordAuthenticationToken(username, null, authorities);

    }

    private Collection<SimpleGrantedAuthority> getAuthorities(String[] roles) {
        return stream(roles).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}