package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.mfa.MFASetupResponseDTO;
import com.microservices.gateway.services.gRPCServices.MfaGRPCService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mfa/")
public class MFAController {

    private final MfaGRPCService mfaGRPCService;

    public MFAController(MfaGRPCService mfaGRPCService) {
        this.mfaGRPCService = mfaGRPCService;
    }

    @PostMapping("/setup")
    public MFASetupResponseDTO setup(@RequestBody Map<String, String> setupRequest){
        return mfaGRPCService.mfaSetup(setupRequest.get("emailAddress"));
    }

    @PostMapping("/confirm")
    public MFASetupResponseDTO confirm(@RequestBody Map<String, String> confirmRequest){
        return mfaGRPCService.mfaConfirm(confirmRequest.get("emailAddress"), confirmRequest.get("code"));
    }

}
