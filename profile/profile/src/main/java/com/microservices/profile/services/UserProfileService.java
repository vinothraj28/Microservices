package com.microservices.profile.services;


import com.microservices.profile.dto.mfa.MFASetupResponse;
import com.microservices.profile.dto.user.UserRequestDTO;
import com.microservices.profile.dto.user.UserResponseDTO;
import com.microservices.profile.dto.user.UserRoleRequestDTO;
import io.jsonwebtoken.Claims;
import java.util.UUID;

public interface UserProfileService {

    UserResponseDTO findByUserId(UUID id);

    UserResponseDTO addUser(UserRequestDTO userRequestDTO);

    //Login moved to Authentication Service
    //String login(String username, String password);

    Claims extractToken(String token);

    boolean addRole(String username, UserRoleRequestDTO userRoleRequestDTO);

//    Moved to MFA service
//    MFASetupResponse mfaSetup(String emailAddress);
//    MFASetupResponse mfaConfirm(String emailAddress, String code);
}
