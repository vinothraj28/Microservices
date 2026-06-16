package com.microservices.profile.jwt;

import com.microservices.profile.services.TokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JWTFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String Authorization = request.getHeader("Authorization");


        if(Authorization==null || Authorization.isBlank() || !Authorization.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        Authorization = Authorization.substring(7);

        try{
            if(!tokenService.isValid(Authorization)){
                log.info("Token: {}", Authorization);

                log.info("Is token valid: {}",
                        tokenService.isValid(Authorization));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            log.info("Extracting claims");
            String userName = tokenService.extractClaims(Authorization).get("email", String.class);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userName,
                            null,
                            List.of()
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch(JwtException ex){
            log.error("JWTException Occurred ", ex);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
