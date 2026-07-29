package com.microservices.gateway.DTOS.sessions;

/**
 * Response after establishing OAuth2 session.
 *
 * @param success Whether session was established successfully
 * @param sessionId Session identifier (for debugging)
 * @param redirectUrl URL to redirect user back to (if provided during login redirect)
 */
public record SessionEstablishResponseDTO(
        boolean success,
        String sessionId,
        String redirectUrl
) {
    public static SessionEstablishResponseDTO success(String sessionId, String redirectUrl) {
        return new SessionEstablishResponseDTO(true, sessionId, redirectUrl);
    }

    public static SessionEstablishResponseDTO success(String sessionId) {
        return new SessionEstablishResponseDTO(true, sessionId, null);
    }
}
