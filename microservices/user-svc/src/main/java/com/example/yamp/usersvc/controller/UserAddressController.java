package com.example.yamp.usersvc.controller;

import com.example.yamp.usersvc.dto.UserAddressDto;
import com.example.yamp.usersvc.service.UserAddressService;
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
@RequestMapping("/api/v1/user-addresses")
@RequiredArgsConstructor
@Tag(name = "UserAddress", description = "The User Address API")
public class UserAddressController {

  private final UserAddressService userAddressService;

  @PostMapping
  @Operation(
      summary = "Create a new user address",
      description = "Creates a new user address and returns the created user address.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "User address created successfully"),
        @ApiResponse(responseCode = "409", description = "User address already exists")
      })
  public ResponseEntity<UserAddressDto> createUserAddress(
      @RequestBody UserAddressDto userAddressDto) {
    UserAddressDto createdUserAddress = userAddressService.createUserAddress(userAddressDto);
    return ResponseEntity.status(201).body(createdUserAddress);
  }

  @GetMapping("/{userId}/{addressId}")
  @Operation(
      summary = "Get user address by ID",
      description = "Retrieves a user address by user ID and address ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User address found"),
        @ApiResponse(responseCode = "404", description = "User address not found")
      })
  public ResponseEntity<UserAddressDto> getUserAddressById(
      @PathVariable UUID userId, @PathVariable UUID addressId) {
    UserAddressDto userAddress = userAddressService.getUserAddressById(userId, addressId);
    return userAddress != null ? ResponseEntity.ok(userAddress) : ResponseEntity.notFound().build();
  }

  @GetMapping
  @Operation(summary = "Get all user addresses", description = "Retrieves all user addresses.")
  @ApiResponse(responseCode = "200", description = "User addresses retrieved successfully")
  public ResponseEntity<List<UserAddressDto>> getAllUserAddresses() {
    List<UserAddressDto> userAddresses = userAddressService.getAllUserAddresses();
    return ResponseEntity.ok(userAddresses);
  }

  @PutMapping
  @Operation(
      summary = "Update a user address",
      description = "Updates an existing user address and returns the updated user address.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User address updated successfully"),
        @ApiResponse(responseCode = "404", description = "User address not found")
      })
  public ResponseEntity<UserAddressDto> updateUserAddress(
      @RequestBody UserAddressDto userAddressDto) {
    UserAddressDto updatedUserAddress = userAddressService.updateUserAddress(userAddressDto);
    return ResponseEntity.ok(updatedUserAddress);
  }

  @DeleteMapping("/{userId}/{addressId}")
  @Operation(
      summary = "Delete a user address",
      description = "Deletes a user address by user ID and address ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User address deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User address not found")
      })
  public ResponseEntity<Void> deleteUserAddress(
      @PathVariable UUID userId, @PathVariable UUID addressId) {
    userAddressService.deleteUserAddress(userId, addressId);
    return ResponseEntity.noContent().build();
  }
}
