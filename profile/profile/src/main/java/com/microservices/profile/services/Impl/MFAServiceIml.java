package com.microservices.profile.services.Impl;

import com.microservices.profile.dto.mfa.MFASetupResponse;
import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.services.MFAService;
import com.microservices.profile.services.QrCodeService;
import com.microservices.profile.services.jwt.TotpService;
import com.microservices.profile.spi.managers.CredentialsManager;
import com.microservices.profile.spi.managers.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MFAServiceIml implements MFAService {

    private final UserManager userManager;
    private final CredentialsManager credentialsManager;
    private final TotpService totpService;
    private final QrCodeService qrCodeService;

    public MFAServiceIml(UserManager userManager, CredentialsManager credentialsManager, TotpService totpService, QrCodeService qrCodeService) {
        this.userManager = userManager;
        this.credentialsManager = credentialsManager;
        this.totpService = totpService;
        this.qrCodeService = qrCodeService;
    }


    @Override
    public MFASetupResponse mfaSetup(String emailAddress) {

        log.info("Setting up MFA for user with email: {}", emailAddress);

        Optional<UserProfile> userProfile = userManager.getUserByEmailAddress(emailAddress);
        if(userProfile.isEmpty()){
            log.error("User with email {} not found", emailAddress);
            throw new UserNotFoundException("User with email " + emailAddress + " not found");
        }

        String secret = totpService.generateSecret();
        credentialsManager.createCredential(userProfile.get(), CredentialsType.TOTP, secret);

        log.info("MFA setup successful for user: {}", emailAddress);
        String qrCode = qrCodeService.generateQrCode(totpService.getUriForTotp(secret, emailAddress));
        return new MFASetupResponse(qrCode);
    }

    @Override
    public MFASetupResponse mfaConfirm(String emailAddress, String code) {

        log.info("Confirming MFA for user with email: {}", emailAddress);

        Optional<UserProfile> userProfile = userManager.getUserByEmailAddress(emailAddress);
        if(userProfile.isEmpty()){
            log.error("User with email {} not found", emailAddress);
            throw new UserNotFoundException("User with email " + emailAddress + " not found");
        }

        if (!credentialsManager.validateCredential(userProfile.get(), CredentialsType.TOTP, code)) {
            log.warn("Failed to confirm MFA: invalid code for user: {}", emailAddress);
            throw new InvalidCredentialsException("Invalid MFA code");
        }

        Credential credentials =
                credentialsManager.updateCredentialStatus(userProfile.get(),
                        CredentialsType.TOTP, CredentialStatus.ACTIVE);

        log.info("MFA confirmed successfully for user: {}", emailAddress);

        return new MFASetupResponse("MFA setup confirmed");
    }
}
