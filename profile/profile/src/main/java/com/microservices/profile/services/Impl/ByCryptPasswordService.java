package com.microservices.profile.services.Impl;

import com.microservices.profile.models.enums.PasswordAlgorithm;
import com.microservices.profile.services.PasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ByCryptPasswordService implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    public ByCryptPasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean validate(String password) {
        return true;
    }

    @Override
    public boolean validate(String password, String userInput) {
        return passwordEncoder.matches(password, userInput);
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean supports(String type) {
        return "ByCrypt".equals(type);
    }

    public String type(){
        return PasswordAlgorithm.BCRYPT.toString();
    }


}
