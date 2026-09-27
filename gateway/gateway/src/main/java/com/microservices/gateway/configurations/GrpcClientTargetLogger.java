package com.microservices.gateway.configurations;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcClientTargetLogger {

    @Value("${grpc.client.profile-service.address}")
    private String profileServiceAddress;

    @Value("${grpc.client.profile-service.negotiationType}")
    private String profileServiceNegotiationType;

    @Value("${grpc.client.address-service.address}")
    private String addressServiceAddress;

    @Value("${grpc.client.address-service.negotiationType}")
    private String addressServiceNegotiationType;

    @Value("${grpc.client.mfa-service.address}")
    private String mfaServiceAddress;

    @Value("${grpc.client.mfa-service.negotiationType}")
    private String mfaServiceNegotiationType;

    @PostConstruct
    void logConfiguredTargets() {
        log.info("gRPC target [profile-service] address={} negotiationType={}",
                profileServiceAddress, profileServiceNegotiationType);
        log.info("gRPC target [address-service] address={} negotiationType={}",
                addressServiceAddress, addressServiceNegotiationType);
        log.info("gRPC target [mfa-service] address={} negotiationType={}",
                mfaServiceAddress, mfaServiceNegotiationType);
    }
}
