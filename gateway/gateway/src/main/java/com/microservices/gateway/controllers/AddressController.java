package com.microservices.gateway.controllers;

import com.microservices.gateway.DTOS.address.AddressRequestDTO;
import com.microservices.gateway.DTOS.address.AddressResponseDTO;
import com.microservices.gateway.services.gRPCServices.AddressGRPCService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address/")
@Tag(name = "User Address APIs")
public class AddressController {

    private final AddressGRPCService addressService;

    public AddressController(AddressGRPCService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Register Address for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address added"),
            @ApiResponse(responseCode = "401", description = "Authorization failed")
    })
    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(@Valid @RequestBody
                                                         AddressRequestDTO addressRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.registerAddress(addressRequestDTO));

    }

    @Operation(summary = "fetch address by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> findByAddressId(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(addressService.findByAddressId(id));
    }

}
