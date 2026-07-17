package com.microservices.profile.grpcService;

import com.microservices.address.grpc.AddressRegisterRequest;
import com.microservices.address.grpc.AddressRegisterResponse;
import com.microservices.address.grpc.AddressServiceGrpc;
import com.microservices.address.grpc.FindAddressRequest;
import com.microservices.profile.dto.address.AddressRequestDTO;
import com.microservices.profile.mappers.AddressMapper;
import com.microservices.profile.services.AddressService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AddressGrpcService extends AddressServiceGrpc.AddressServiceImplBase {
    private final AddressService addressService;
    private final AddressMapper addressMapper;


    public AddressGrpcService(AddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @Override
    public void registerAddress(
            AddressRegisterRequest request,
            StreamObserver<AddressRegisterResponse> responseObserver
    ) {
       AddressRequestDTO addressRequestDTO = addressMapper.toDTO(request);
       AddressRegisterResponse registerResponse = addressMapper
               .toGrpcResponse(addressService.addAddress(addressRequestDTO));
        responseObserver.onNext(registerResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void findByAddressId(
           FindAddressRequest request,
            StreamObserver<AddressRegisterResponse> responseObserver
    ) {
        Long addressId = Long.parseLong(request.getAddressId());
        AddressRegisterResponse grpcResponse = addressMapper
                .toGrpcResponse(addressService.findByAddressId(addressId));
        responseObserver.onNext(grpcResponse);
        responseObserver.onCompleted();
    }
}
