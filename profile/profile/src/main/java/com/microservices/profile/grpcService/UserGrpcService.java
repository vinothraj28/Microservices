package com.microservices.profile.grpcService;

import com.microservices.profile.dto.user.UserResponseDTO;
import com.microservices.profile.grpc.*;
import com.microservices.profile.mappers.UserProfileMapper;
import com.microservices.profile.services.UserProfileService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    public UserGrpcService(UserProfileService userProfileService, UserProfileMapper userProfileMapper) {
        this.userProfileService = userProfileService;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public void login(
            LoginRequest request,
            StreamObserver<LoginResponse> responseObserver
    ) {

        String token =
                userProfileService.login(
                        request.getUsername(),
                        request.getPassword()
                );

        LoginResponse response =
                LoginResponse.newBuilder()
                        .setToken(token)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void register(RegisterRequest request,
                         StreamObserver<RegisterResponse> responseObserver) {
        UserResponseDTO user= userProfileService.addUser(userProfileMapper.toDTO(request));
        RegisterResponse registerResponse = RegisterResponse.newBuilder()
                .setUserId(user.userId().toString())
                .setUserName(user.userName())
                .setEmailAddress(user.emailAddress())
                .setDob(user.dob().toString())
                .addAllRoles(user.roles())
                .build();
        responseObserver.onNext(registerResponse);
        responseObserver.onCompleted();
    }
}
