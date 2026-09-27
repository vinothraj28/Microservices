package com.microservices.gateway.services.gRPCServices;

import com.microservices.gateway.DTOS.auth.LoginRequestDTO;
import com.microservices.gateway.DTOS.register.RegisterRequestDTO;
import com.microservices.gateway.DTOS.register.RegisterResponseDTO;
import com.microservices.gateway.excpetions.DuplicateEmailException;
import com.microservices.gateway.excpetions.UserNotFoundException;
import com.microservices.gateway.mappers.UserMapper;
import com.microservices.profile.grpc.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserGRPCService {

    //private static final Logger log = LoggerFactory.getLogger(UserGRPCService.class);

    @GrpcClient("profile-service")
    private UserServiceGrpc
            .UserServiceBlockingStub userServiceBlockingStub;
    private final UserMapper userMapper;

    public UserGRPCService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

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

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO){
        log.info("Registering user via gRPC {}", registerRequestDTO.userName());

        try{
            RegisterRequest request = RegisterRequest.newBuilder()
                    .setUserName(registerRequestDTO.userName())
                    .setEmailAddress(registerRequestDTO.emailAddress())
                    .setDob(registerRequestDTO.dob().toString())
                    .setPassword(registerRequestDTO.password())
                    .build();
            RegisterResponse registerResponse = userServiceBlockingStub.register(request);
            log.info("gRPC response received for user registration {}", registerResponse);
            return userMapper.toRegisterResponseDTO(registerResponse);
        }catch (StatusRuntimeException ex){
           Status.Code code = ex.getStatus().getCode();
            String description = ex.getStatus().getDescription();
            log.error("gRPC register failed. code={}, description={}", code, description, ex);

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
                default -> throw ex;
            }
        }
    }
}
