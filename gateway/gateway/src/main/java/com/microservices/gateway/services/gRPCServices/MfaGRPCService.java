package com.microservices.gateway.services.gRPCServices;


import com.microservices.gateway.DTOS.mfa.MFASetupResponseDTO;
import com.microservices.profile.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MfaGRPCService {

    @GrpcClient("mfa-service")
    private MfaServiceGrpc.MfaServiceBlockingStub mfaServiceBlockingStub;

    public MFASetupResponseDTO mfaSetup(String emailAddress){
        log.info("Fetching mfa setup for the user {}", emailAddress);
        MfaSetupRequest mfaSetupRequest = MfaSetupRequest.newBuilder().setEmailAddress(emailAddress).build();
        log.info("Fetching mfa setup for the user {}", emailAddress);
        MfaSetupResponse mfaSetupResponse = mfaServiceBlockingStub.mfaSetup(mfaSetupRequest);
        log.info("mfa response received for the user {} {}", emailAddress, mfaSetupResponse);
        return new MFASetupResponseDTO(mfaSetupResponse.getQrCodeUrl());
    }

    public MFASetupResponseDTO mfaConfirm(String emailAddress, String code){
        MfaConfirmRequest mfaConfirmRequest = MfaConfirmRequest.newBuilder().setEmailAddress(emailAddress)
                .setCode(code).build();
        MfaConfirmResponse mfaConfirmResponse = mfaServiceBlockingStub.mfaConfirm(mfaConfirmRequest);
        return new MFASetupResponseDTO(mfaConfirmResponse.getMfaStatus());
    }

}
