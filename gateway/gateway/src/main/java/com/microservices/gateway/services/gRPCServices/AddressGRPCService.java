package com.microservices.gateway.services.gRPCServices;

import com.microservices.address.grpc.*;

import com.microservices.gateway.DTOS.address.AddressRequestDTO;
import com.microservices.gateway.DTOS.address.AddressResponseDTO;

import com.microservices.gateway.mappers.AddressMapper;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AddressGRPCService {

    @GrpcClient("address-service")
    private AddressServiceGrpc.AddressServiceBlockingStub addressServiceBlockingStub;
    private final AddressMapper addressMapper;

    public AddressGRPCService(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }


    public AddressResponseDTO registerAddress(AddressRequestDTO request){
        log.info("Registering address via gRPC for the user {} ", request.userId());
        AddressRegisterRequest grpcRequest =
                AddressRegisterRequest.newBuilder()
                        .setAddress(Address
                                .newBuilder()
                                .setAddressType(AddressType.valueOf(request.addressType().toString()))
                                .setCountry(request.country())
                                .setProvince(request.province())
                                .setStreet(request.street())
                                .setZipcode(request.zipcode())
                                .setUserId(request.userId().toString())
                                .build() )
                        .build();

        AddressRegisterResponse grpcResponse =
                addressServiceBlockingStub.registerAddress(grpcRequest);
//        AddressResponseDTO responseDTO = new AddressResponseDTO(
//                grpcResponse.getAddressId(),
//                grpcResponse.getCountry(),
//                grpcResponse.getProvince(),
//                grpcResponse.getStreet(),
//                grpcResponse.getZipcode(),
//                UUID.fromString(grpcResponse.getUserId())
//        );
        return addressMapper.toAddressResponseDTO(grpcResponse);
    }


    public  AddressResponseDTO findByAddressId(Long addressId){
        log.info("Finding address via gRPC for the address id {} ", addressId);
        FindAddressRequest grpcRequest =
                FindAddressRequest.newBuilder()
                        .setAddressId(addressId.toString())
                        .build();

        AddressRegisterResponse grpcResponse =
                addressServiceBlockingStub.findByAddressId(grpcRequest);
//        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(
//                grpcResponse.getAddressId(),
//                grpcResponse.getCountry(),
//                grpcResponse.getProvince(),
//                grpcResponse.getStreet(),
//                grpcResponse.getZipcode(),
//                UUID.fromString(grpcResponse.getUserId())
//        );
//        return addressResponseDTO;
        return addressMapper.toAddressResponseDTO(grpcResponse);
    }

}
