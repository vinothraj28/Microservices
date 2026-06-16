package com.microservices.profile.services;


import com.microservices.profile.dto.address.AddressRequestDTO;
import com.microservices.profile.dto.address.AddressResponseDTO;

public interface AddressService {

    AddressResponseDTO findByAddressId(Long id);
    AddressResponseDTO addAddress(AddressRequestDTO addressRequestDTO);
}
