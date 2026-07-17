package com.microservices.profile.encryption;

public interface EncryptionService {

    String encrypt(String plainText);
    String decrypt(String cipherText);

}
