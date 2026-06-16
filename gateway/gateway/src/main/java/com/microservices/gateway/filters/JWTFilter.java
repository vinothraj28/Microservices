package com.microservices.gateway.filters;

import com.microservices.gateway.services.jwts.JWTService;
import io.jsonwebtoken.JwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JWTFilter implements GlobalFilter {

    private final JWTService jwtService;

    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
            GatewayFilterChain chain) {
        String path = exchange
                .getRequest()
                .getURI()
                .getPath();

        if(path.equals("/api/v1/users/login") || path.contains("/api/v1/users//register")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange
                .getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        String token = authHeader.substring(7);
        try{
            if(!jwtService.isValid(token)){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String email = jwtService.extractClaims(token).get("email", String.class);
            List<String> roles = jwtService.extractClaims(token).get("roles", List.class);

            String rolesHeader = String.join(",", roles);
            exchange = exchange.mutate()
                    .request(r -> r.headers( httpHeaders -> {
                        httpHeaders.remove(HttpHeaders.AUTHORIZATION);

                        httpHeaders.remove("X-User-Email");
                        httpHeaders.remove("X-User-Roles");

                        httpHeaders.add("X-User-Email", email);
                        httpHeaders.add("X-User-Roles", rolesHeader);
                    })

                    )
                    .build();


            return chain.filter(exchange);
        }catch (JwtException ex){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }


}
