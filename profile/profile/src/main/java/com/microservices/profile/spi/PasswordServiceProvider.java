package com.microservices.profile.spi;

import com.microservices.profile.services.PasswordService;

public interface PasswordServiceProvider {
    PasswordService get(String type);
}
