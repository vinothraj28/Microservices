package com.microservices.gateway.mappers;

import com.microservices.gateway.DTOS.register.RegisterResponseDTO;
import com.microservices.profile.grpc.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(registerResponse.getRolesList()))")
    RegisterResponseDTO toRegisterResponseDTO(RegisterResponse registerResponse);

    default Set<String> mapRoles(List<String> roles ){
        return new HashSet<>(roles);
    }
}
