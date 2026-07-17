package com.microservices.gateway.mappers;

import com.microservices.address.grpc.AddressRegisterResponse;
import com.microservices.gateway.DTOS.address.AddressResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "userId", expression = "java(UUID.fromString(addressRegisterResponse.getUserId().toString()))")
    AddressResponseDTO toAddressResponseDTO(AddressRegisterResponse addressRegisterResponse);

}
