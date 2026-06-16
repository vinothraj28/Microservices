package com.microservices.profile.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
