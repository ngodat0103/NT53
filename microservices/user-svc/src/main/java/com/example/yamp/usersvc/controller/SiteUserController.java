package com.example.yamp.usersvc.controller;

import com.example.yamp.usersvc.dto.SiteUserDto;
import com.example.yamp.usersvc.service.SiteUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "SiteUser", description = "The Site User API")
public class SiteUserController {

  private final SiteUserService siteUserService;

  @PostMapping
  @Operation(
      summary = "Create a new user",
      description = "Creates a new user and returns the created user.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "409", description = "User with this email already exists")
      })

  public ResponseEntity<SiteUserDto> createUser(@RequestBody @Valid SiteUserDto siteUserDto) {
    SiteUserDto createdUser = siteUserService.createUser(siteUserDto);
    return ResponseEntity.status(201).body(createdUser);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  public ResponseEntity<SiteUserDto> getUserById(@PathVariable UUID id) {
    SiteUserDto user = siteUserService.getUserById(id);
    return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
  }

  @GetMapping
  @Operation(summary = "Get all users", description = "Retrieves all users.")
  @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
  public ResponseEntity<List<SiteUserDto>> getAllUsers() {
    List<SiteUserDto> users = siteUserService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a user",
      description = "Updates an existing user and returns the updated user.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  public ResponseEntity<SiteUserDto> updateUser(@RequestBody SiteUserDto siteUserDto, @PathVariable UUID id) {
    SiteUserDto updatedUser = siteUserService.updateUser(siteUserDto,id);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a user", description = "Deletes a user by their ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    siteUserService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
