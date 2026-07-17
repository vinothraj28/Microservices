package com.microservices.profile.services;

public interface PasswordService {

    boolean validate(String password, String userInput);
    String encode(String password);
    boolean supports(String type);
    String type();
}
