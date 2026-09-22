package com.microservices.gateway.DTOS.search;

public record SearchSuggestionDTO(
        String id,
        String type,
        String title,
        String subtitle
) {
}
