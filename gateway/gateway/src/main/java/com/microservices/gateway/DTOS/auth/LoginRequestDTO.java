package com.microservices.gateway.DTOS.auth;

public record LoginRequestDTO(
        String username,
        String password
) {

}
