package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.mfa.MFASetupResponseDTO;
import com.microservices.gateway.services.gRPCServices.MfaGRPCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mfa/")
public class MFAController {

    private final MfaGRPCService mfaGRPCService;

    public MFAController(MfaGRPCService mfaGRPCService) {
        this.mfaGRPCService = mfaGRPCService;
    }

    @PostMapping("/setup")
    public Mono<ResponseEntity<MFASetupResponseDTO>> setup(@RequestBody Map<String, String> setupRequest){
        return Mono.fromCallable(() -> mfaGRPCService.mfaSetup(setupRequest.get("emailAddress")))
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
        //return Mono.just(ResponseEntity.ok(mfaGRPCService.mfaSetup(setupRequest.get("emailAddress"))));
    }

    @PostMapping("/confirm")
    public Mono<ResponseEntity<MFASetupResponseDTO>> confirm(@RequestBody Map<String, String> confirmRequest){
        return Mono.fromCallable(() -> mfaGRPCService.mfaConfirm(confirmRequest.get("emailAddress"), confirmRequest.get("code")))
                .map(ResponseEntity::ok)
                .subscribeOn(Schedulers.boundedElastic());
        //return Mono.just( ResponseEntity.ok(mfaGRPCService.mfaConfirm(confirmRequest.get("emailAddress"), confirmRequest.get("code"))));
    }

}
