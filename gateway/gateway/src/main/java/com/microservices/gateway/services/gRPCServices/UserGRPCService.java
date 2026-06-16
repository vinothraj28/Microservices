package com.microservices.gateway.services.gRPCServices;

import com.microservices.gateway.DTOS.LoginRequestDTO;
import com.microservices.gateway.DTOS.RegisterRequestDTO;
import com.microservices.gateway.controllers.UserController;
import com.microservices.gateway.excpetions.user.DuplicateEmailException;
import com.microservices.gateway.excpetions.user.UserNotFoundException;
import com.microservices.profile.grpc.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ValidationException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;


@Service
public class UserGRPCService {

    private static final Logger log = LoggerFactory.getLogger(UserGRPCService.class);

    @GrpcClient("profile-service")
    private UserServiceGrpc
            .UserServiceBlockingStub userServiceBlockingStub;

    public LoginResponse login(LoginRequestDTO request){
       log.info("Fetching login via gRPC for the user {} ", request.username());
        System.out.println("Fetching login via gRPC for the user");
        LoginRequest grpcRequest =
                LoginRequest.newBuilder()
                        .setUsername(request.username())
                        .setPassword(request.password())
                        .build();

        LoginResponse grpcResponse =
                userServiceBlockingStub.login(grpcRequest);
        System.out.println("gRPC response received ");
        log.info("gRPC response received",grpcResponse);
        return grpcResponse;
    }

    public RegisterResponse register(RegisterRequestDTO registerRequestDTO){
        log.info("Registering user via gRPC", registerRequestDTO.userName());

        try{
            RegisterRequest request = RegisterRequest.newBuilder()
                    .setUserName(registerRequestDTO.userName())
                    .setEmailAddress(registerRequestDTO.emailAddress())
                    .setDob(registerRequestDTO.dob().toString())
                    .setPassword(registerRequestDTO.password())
                    .build();
            return userServiceBlockingStub.register(request);

        }catch (StatusRuntimeException ex){
           Status.Code code = ex.getStatus().getCode();

            switch (code) {

                case ALREADY_EXISTS ->
                        throw new DuplicateEmailException(
                                ex.getStatus().getDescription());

                case NOT_FOUND ->
                        throw new UserNotFoundException(
                                ex.getStatus().getDescription());

                case INVALID_ARGUMENT ->
                        throw new ValidationException(
                                ex.getStatus().getDescription());
                default ->
                        throw new RuntimeException(
                                "Internal gRPC error");
            }
        }
    }
}
