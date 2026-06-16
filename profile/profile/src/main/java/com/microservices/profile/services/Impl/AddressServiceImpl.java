package com.microservices.profile.services.Impl;

import com.microservices.profile.dto.address.AddressRequestDTO;
import com.microservices.profile.dto.address.AddressResponseDTO;
import com.microservices.profile.exceptions.AddressNotFoundException;
import com.microservices.profile.exceptions.DatabaseOperationException;
import com.microservices.profile.exceptions.UserNotFoundException;
import com.microservices.profile.mappers.AddressMapper;
import com.microservices.profile.models.entities.Address;
import com.microservices.profile.models.entities.UserProfile;
import com.microservices.profile.repository.AddressRepository;
import com.microservices.profile.repository.UserProfileRepository;
import com.microservices.profile.services.AddressService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserProfileRepository userProfileRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, UserProfileRepository userProfileRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userProfileRepository = userProfileRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public AddressResponseDTO findByAddressId(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow( ()-> new AddressNotFoundException("Address not found")
         );
        return addressMapper.toDTO(address);
    }

    @Transactional
    @Override
    public AddressResponseDTO addAddress(AddressRequestDTO addressRequestDTO) {
        log.debug("Fetching user for address creation: {}", addressRequestDTO.userId());
        UserProfile userProfile = userProfileRepository.findById(addressRequestDTO.userId()).orElseThrow(
                () -> new UserNotFoundException("User Not Found")
        );
        Address address = addressMapper.toEntity(addressRequestDTO);
        address.setUserProfile(userProfile);

        log.info("Creating address for userId: {}", addressRequestDTO.userId());
        Address saved = addressRepository.save(address);
        return addressMapper.toDTO(saved);
    }
}
