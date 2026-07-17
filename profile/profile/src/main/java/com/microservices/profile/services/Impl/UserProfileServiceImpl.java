package com.microservices.profile.services.Impl;

import com.microservices.profile.common.RoleAssigner;
import com.microservices.profile.dto.mfa.MFASetupResponse;
import com.microservices.profile.dto.user.UserResponseDTO;
import com.microservices.profile.dto.user.UserRequestDTO;
import com.microservices.profile.dto.user.UserRoleRequestDTO;
import com.microservices.profile.exceptions.InvalidCredentialsException;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.mappers.UserProfileMapper;
import com.microservices.profile.models.entities.Credential;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.CredentialStatus;
import com.microservices.profile.models.enums.CredentialsType;
import com.microservices.profile.models.enums.PasswordAlgorithm;
import com.microservices.profile.repository.UserProfileRepository;
import com.microservices.profile.services.PasswordService;
import com.microservices.profile.services.QrCodeService;
import com.microservices.profile.services.TokenService;
import com.microservices.profile.services.UserProfileService;
import com.microservices.profile.services.jwt.TotpService;
import com.microservices.profile.spi.managers.CredentialsManager;
import com.microservices.profile.spi.managers.UserManager;
import com.microservices.profile.spi.registry.CredentialProviderRegistry;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileMapper userProfileMapper;
    private final TokenService tokenService;
    private final RoleAssigner roleAssigner;
    private final CredentialsManager credentialsManager;
    private final UserManager userManager;
    private final TotpService totpService;
    private final QrCodeService qrCodeService;

    public UserProfileServiceImpl(UserProfileMapper userProfileMapper,
                                  TokenService tokenService, RoleAssigner roleAssigner, CredentialsManager credentialsManager, UserManager userManager, TotpService totpService, QrCodeService qrCodeService){

        this.userProfileMapper = userProfileMapper;
        this.tokenService = tokenService;
        this.roleAssigner = roleAssigner;
        this.credentialsManager = credentialsManager;
        this.userManager = userManager;
        this.totpService = totpService;
        this.qrCodeService = qrCodeService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByUserId(UUID id){
        log.debug("Fetching user with id: {}", id);
        UserProfile userProfile = userManager.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userProfileMapper.toDTO(userProfile);
    }

    @Override
    @Transactional
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        log.info("Creating user with email: {}", userRequestDTO.emailAddress());
        UserProfile userProfile = userProfileMapper.toEntity(userRequestDTO);


        userProfile.setRoles(roleAssigner.assignRoles() );
        log.info("Creating user with email with userManager: {}", userRequestDTO.emailAddress());
        UserProfile user = userManager.createUser(userProfile);

        log.info("Creating credentials with email: {}", userRequestDTO.emailAddress());
        credentialsManager.createCredential(user, CredentialsType.PASSWORD, userRequestDTO.password());
        return userProfileMapper.toDTO(user);
    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public String login(String email, String password) {
//        log.info("Login attempt for email: {}", email);
//
//        //Optional<UserProfile> userProfile = userProfileRepository.findByEmailAddress(email);
//        Optional<UserProfile> userProfile = userManager.getUserByEmailAddress(email);
//        if (userProfile.isEmpty()) {
//            log.warn("Login failed: user not found for email: {}", email);
//            throw new InvalidCredentialsException("Email or password is incorrect");
//        }
//
////        PasswordService passwordService = passwordServiceProvider.get(PasswordAlgorithm.BCRYPT.toString());
////
////
////        if (!passwordService.validate(password, userProfile.get().getPassword())) {
////            log.warn("Login failed: invalid password for email: {}", email);
////            throw new InvalidCredentialsException("Email or password is incorrect");
////        }
//        if(!credentialsManager.validateCredential(userProfile.get(), CredentialsType.PASSWORD, password)){
//            log.warn("Login failed: invalid password for email: {}", email);
//            throw new InvalidCredentialsException("Email or password is incorrect");
//        }
//
//        log.info("Login successful for email: {}", email);
//        return tokenService.generateToken(userProfile.get());
//    }

    @Override
    @Transactional
    public boolean addRole(String email, UserRoleRequestDTO userRoleRequestDTO) {
        log.info("Adding role to user: {}", email);

        //Optional<UserProfile> userProfile = userProfileRepository.findByEmailAddress(email);
        Optional<UserProfile> userProfile = userManager.getUserByEmailAddress(email);
        if (userProfile.isEmpty()) {
            log.warn("Failed to add role: user not found for email: {}", email);
            throw new UserNotFoundException("User not found");
        }
        userProfile.get().getRoles().addAll(userRoleRequestDTO.role());
        //userProfileRepository.save(userProfile.get());
        userManager.updateUser(userProfile.get());
        log.info("Role added successfully for user: {}", email);
        return true;
    }
//
//    @Transactional
//    public MFASetupResponse mfaSetup(String emailAddress){
//        log.info("Setting up MFA for user: {}", emailAddress);
//        Optional<UserProfile> userProfile = userManager.getUserByEmailAddress(emailAddress);
//        if (userProfile.isEmpty()) {
//            log.warn("Failed to setup MFA: user not found for email: {}", emailAddress);
//            throw new UserNotFoundException("User not found");
//        }
//        String secret = totpService.generateSecret();
//        //Creates credential with status as PENDING
//        credentialsManager.createCredential(userProfile.get(), CredentialsType.TOTP, secret);
//        log.info("MFA setup successful for user: {}", emailAddress);
//        String qrCode = qrCodeService.generateQrCode(totpService.getUriForTotp(secret, emailAddress));
//        return new MFASetupResponse(qrCode);
//    }
//
//    @Transactional
//    public MFASetupResponse mfaConfirm(String emailAddress, String code){
//
//        log.info("Confirming MFA for user: {}", emailAddress);
//        Optional<UserProfile> userProfile = userManager.getUserByEmailAddress(emailAddress);
//        if (userProfile.isEmpty()) {
//            log.warn("Failed to confirm MFA: user not found for email: {}", emailAddress);
//            throw new UserNotFoundException("User not found");
//        }
//
//        if (!credentialsManager.validateCredential(userProfile.get(), CredentialsType.TOTP, code)) {
//            log.warn("Failed to confirm MFA: invalid code for user: {}", emailAddress);
//            throw new InvalidCredentialsException("Invalid MFA code");
//        }
//        //Updates credential status as Active
//        Credential totpCredential = credentialsManager.updateCredentialStatus(userProfile.get(),
//                CredentialsType.TOTP, CredentialStatus.ACTIVE);
//        log.info("MFA confirmed successfully for user: {}", emailAddress);
//
//        return new MFASetupResponse("MFA setup confirmed");
//    }

    @Override
    @Transactional(readOnly = true)
    public Claims extractToken(String token){
        return tokenService.extractClaims(token);
    }
}


