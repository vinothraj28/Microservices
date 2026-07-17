package com.microservices.profile.encryption;

import com.microservices.profile.exceptions.EncryptionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AESEncryptionService implements EncryptionService {

    @Value("${security.encryption.key}")
    private String masterKey;

    @Override
    public String encrypt(String plaintext) {

        try {

            byte[] iv = generateIV();

            Cipher cipher = Cipher.getInstance(
                    "AES/GCM/NoPadding"
            );

            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(
                            128,
                            iv
                    );

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    getSecretKey(),
                    parameterSpec
            );


            byte[] encrypted =
                    cipher.doFinal(
                            plaintext.getBytes(StandardCharsets.UTF_8)
                    );


            byte[] combined =
                    new byte[iv.length + encrypted.length];


            System.arraycopy(
                    iv,
                    0,
                    combined,
                    0,
                    iv.length
            );


            System.arraycopy(
                    encrypted,
                    0,
                    combined,
                    iv.length,
                    encrypted.length
            );


            return Base64.getEncoder()
                    .encodeToString(combined);


        } catch (Exception e) {

            throw new EncryptionException(
                    "Failed to encrypt value"
            );
        }
    }


    @Override
    public String decrypt(String encryptedText) {

        try {

            byte[] decoded =
                    Base64.getDecoder()
                            .decode(encryptedText);


            byte[] iv = new byte[12];

            byte[] cipherText =
                    new byte[decoded.length - 12];


            System.arraycopy(
                    decoded,
                    0,
                    iv,
                    0,
                    12
            );


            System.arraycopy(
                    decoded,
                    12,
                    cipherText,
                    0,
                    cipherText.length
            );


            Cipher cipher =
                    Cipher.getInstance(
                            "AES/GCM/NoPadding"
                    );


            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(
                            128,
                            iv
                    );


            cipher.init(
                    Cipher.DECRYPT_MODE,
                    getSecretKey(),
                    parameterSpec
            );


            byte[] decrypted =
                    cipher.doFinal(cipherText);


            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );


        } catch (Exception e) {

            throw new EncryptionException(
                    "Failed to decrypt value"
            );
        }
    }

    private SecretKey getSecretKey() {
        byte[] decoded = Base64.getDecoder().decode(masterKey);
        return new SecretKeySpec(decoded, "AES");
    }

    private byte[] generateIV() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

}
