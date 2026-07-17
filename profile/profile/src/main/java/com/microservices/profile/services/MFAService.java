package com.microservices.profile.services;

import com.microservices.profile.dto.mfa.MFASetupResponse;

public interface MFAService {

    MFASetupResponse mfaSetup(String emailAddress);
    MFASetupResponse mfaConfirm(String emailAddress, String code);

}
