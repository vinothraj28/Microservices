package com.microservices.profile.grpcService;

import com.microservices.profile.dto.mfa.MFASetupResponse;
import com.microservices.profile.grpc.*;
import com.microservices.profile.services.MFAService;
import com.microservices.profile.services.UserProfileService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class MfaService extends MfaServiceGrpc.MfaServiceImplBase {

    private final MFAService mfaService;

    public MfaService(MFAService userProfileService, MFAService mfaService) {
        this.mfaService = mfaService;

    }

    @Override
    public void mfaSetup(MfaSetupRequest request, StreamObserver<MfaSetupResponse> responseObserver) {
        MFASetupResponse mfaSetupResponse = mfaService.mfaSetup(request.getEmailAddress());
        MfaSetupResponse mfaSetupResponse1 = MfaSetupResponse.newBuilder()
                .setQrCodeUrl(mfaSetupResponse.qrCodeUrl()).build();
        responseObserver.onNext(mfaSetupResponse1);
        responseObserver.onCompleted();
    }

    @Override
    public void mfaConfirm(MfaConfirmRequest request, StreamObserver<MfaConfirmResponse> responseObserver) {
        MFASetupResponse mfaSetupResponse = mfaService.mfaConfirm(request.getEmailAddress(), request.getCode());
        MfaConfirmResponse mfaConfirmResponse = MfaConfirmResponse.newBuilder()
                .setMfaStatus(mfaSetupResponse.qrCodeUrl()).build();
        responseObserver.onNext(mfaConfirmResponse);
        responseObserver.onCompleted();
    }
}
