package com.microservices.profile.mappers;

import com.microservices.profile.dto.address.AddressRequestDTO;
import com.microservices.profile.dto.address.AddressResponseDTO;
import com.microservices.profile.models.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "userProfile", ignore = true)
    public Address toEntity(AddressRequestDTO addressRequestDTO);

    @Mapping(target = "userId", source = "userProfile.userId")
    public AddressResponseDTO toDTO(Address address);

}
