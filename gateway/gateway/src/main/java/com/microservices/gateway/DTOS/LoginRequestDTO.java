package com.microservices.gateway.DTOS;

public record LoginRequestDTO(
        String username,
        String password
) {

}
