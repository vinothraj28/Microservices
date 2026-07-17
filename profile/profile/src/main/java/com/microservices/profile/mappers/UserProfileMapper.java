package com.microservices.profile.mappers;

import com.microservices.profile.dto.user.UserResponseDTO;
import com.microservices.profile.dto.user.UserRequestDTO;
import com.microservices.profile.grpc.RegisterRequest;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.models.enums.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(userProfile.getRoles()))")
    public UserResponseDTO toDTO(UserProfile userProfile);

    public UserProfile toEntity(UserRequestDTO userRequestDTO);

    public UserRequestDTO toDTO(RegisterRequest request);


    default Set<String> mapRoles( Set<Roles> roles ){
        return roles.stream().map(Roles::name)
                .collect(Collectors.toSet());
    }
}
