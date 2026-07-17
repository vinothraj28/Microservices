package com.microservices.profile.mappers;

import com.microservices.address.grpc.AddressRegisterRequest;
import com.microservices.address.grpc.AddressRegisterResponse;
import com.microservices.profile.dto.address.AddressRequestDTO;
import com.microservices.profile.dto.address.AddressResponseDTO;
import com.microservices.profile.models.entities.Address;
import com.microservices.profile.models.enums.AddressType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "userProfile", ignore = true)
    public Address toEntity(AddressRequestDTO addressRequestDTO);

    @Mapping(target = "userId", source = "userProfile.userId")
    public AddressResponseDTO toDTO(Address address);

    @Mapping(target = "addressType", source = "address.addressType")
    @Mapping(target = "zipcode", source = "address.zipcode")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "userId",
            expression = "java(java.util.UUID.fromString(request.getAddress().getUserId()))")
    public AddressRequestDTO toDTO(AddressRegisterRequest request);

    @Mapping(target = "addressId",
            expression = "java(addressResponseDTO.addressId())")
    @Mapping(target = "userId",
            expression = "java(addressResponseDTO.userId().toString())")
    public AddressRegisterResponse toGrpcResponse(AddressResponseDTO addressResponseDTO);

    default AddressType map(
            com.microservices.address.grpc.AddressType type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case HOME -> AddressType.HOME;
            case WORK -> AddressType.WORK;
            case BILLING -> AddressType.BILLING;
            case SHIPPING -> AddressType.SHIPPING;
            case UNRECOGNIZED -> null;
        };
    }

}
