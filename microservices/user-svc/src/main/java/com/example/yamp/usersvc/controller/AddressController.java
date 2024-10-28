package com.example.yamp.usersvc.controller;

import com.example.yamp.usersvc.dto.AddressDto;
import com.example.yamp.usersvc.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Tag(name = "Address", description = "The Address API")
public class AddressController {

  private final AddressService addressService;

  @PostMapping
  @Operation(
      summary = "Create a new address",
      description = "Creates a new address and returns the created address.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Address created successfully"),
        @ApiResponse(responseCode = "409", description = "Address already exists")
      })
  public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto) {
    AddressDto createdAddress = addressService.createAddress(addressDto);
    return ResponseEntity.status(201).body(createdAddress);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get address by ID", description = "Retrieves an address by its ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Address found"),
        @ApiResponse(responseCode = "404", description = "Address not found")
      })
  public ResponseEntity<AddressDto> getAddressById(@PathVariable UUID id) {
    AddressDto address = addressService.getAddressById(id);
    return address != null ? ResponseEntity.ok(address) : ResponseEntity.notFound().build();
  }

  @GetMapping
  @Operation(summary = "Get all addresses", description = "Retrieves all addresses.")
  @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
  public ResponseEntity<List<AddressDto>> getAllAddresses() {
    List<AddressDto> addresses = addressService.getAllAddresses();
    return ResponseEntity.ok(addresses);
  }

  @PutMapping
  @Operation(
      summary = "Update an address",
      description = "Updates an existing address and returns the updated address.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Address updated successfully"),
        @ApiResponse(responseCode = "404", description = "Address not found")
      })
  public ResponseEntity<AddressDto> updateAddress(@RequestBody AddressDto addressDto) {
    AddressDto updatedAddress = addressService.updateAddress(addressDto);
    return ResponseEntity.ok(updatedAddress);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an address", description = "Deletes an address by its ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Address not found")
      })
  public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
    addressService.deleteAddress(id);
    return ResponseEntity.noContent().build();
  }
}
