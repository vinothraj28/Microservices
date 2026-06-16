package com.microservices.profile.services.Impl;

import com.microservices.profile.common.RoleAssigner;
import com.microservices.profile.dto.user.UserResponseDTO;
import com.microservices.profile.dto.user.UserRequestDTO;
import com.microservices.profile.dto.user.UserRoleRequestDTO;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.mappers.UserProfileMapper;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.PasswordAlgorithm;
import com.microservices.profile.models.enums.Roles;
import com.microservices.profile.repository.UserProfileRepository;
import com.microservices.profile.services.PasswordService;
import com.microservices.profile.services.TokenService;
import com.microservices.profile.services.UserProfileService;
import com.microservices.profile.spi.PasswordServiceProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final PasswordServiceProvider passwordServiceProvider;
    private final TokenService tokenService;
    private final RoleAssigner roleAssigner;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository,
                                  UserProfileMapper userProfileMapper, PasswordServiceProvider passwordServiceProvider, TokenService tokenService, RoleAssigner roleAssigner){
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.passwordServiceProvider = passwordServiceProvider;
        this.tokenService = tokenService;
        this.roleAssigner = roleAssigner;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByUserId(UUID id){
        log.debug("Fetching user with id: {}", id);
        UserProfile userProfile =  userProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userProfileMapper.toDTO(userProfile);
    }

    @Override
    @Transactional
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        log.info("Creating user with email: {}", userRequestDTO.emailAddress());
        UserProfile userProfile = userProfileMapper.toEntity(userRequestDTO);
        userProfile.setPassword( passwordServiceProvider.
                get(PasswordAlgorithm.BCRYPT.toString()).
                encode(userProfile.getPassword()) );

        userProfile.setRoles( roleAssigner.assignRoles() );
        UserProfile user = userProfileRepository.save(userProfile);
        return userProfileMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public String login(String username, String password){
        log.info("Fetching user email for login: {}", username);
        UserProfile userProfile = userProfileRepository.findByEmailAddress(username);
        log.debug("Fetching login for the user "+userProfile.getEmailAddress());

        if(userProfile.getPassword()!=null &&
                passwordServiceProvider.get("ByCrypt").validate(password, userProfile.getPassword())){
           return tokenService.generateToken(userProfile);
        }
        return "Username/Password incorrect";
    }

    @Override
    @Transactional
    public boolean addRole(String username, UserRoleRequestDTO userRoleRequestDTO){
        log.info("Fetching user for role creation: {}", username);

        UserProfile userProfile =  userProfileRepository.findByEmailAddress(username);
        if(userProfile==null){
            throw new UserNotFoundException("User not found");
        }
       userProfile.getRoles().addAll(userRoleRequestDTO.role());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Claims extractToken(String token){
        return tokenService.extractClaims(token);
    }

}


