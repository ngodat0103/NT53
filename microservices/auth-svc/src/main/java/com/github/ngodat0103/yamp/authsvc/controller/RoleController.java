package com.github.ngodat0103.yamp.authsvc.controller;

import com.github.ngodat0103.yamp.authsvc.dto.RoleDto;
import com.github.ngodat0103.yamp.authsvc.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/roles")
@AllArgsConstructor
@SecurityRequirement(name = "oauth2")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

  private RoleService roleService;

  @Operation(summary = "Get all roles", description = "Retrieves a list of all roles.")
  @GetMapping(produces = "application/json")
  public Set<RoleDto> getRole() {
    return roleService.getRole();
  }

  @Operation(summary = "Create a new role", description = "Adds a new role to the system.")
  @ApiResponse(responseCode = "201", description = "Role created successfully")
  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public void createRole(@RequestBody RoleDto roleDto) {
    log.debug("Controller createRole method called");
    roleService.addRole(roleDto);
  }

  @Operation(summary = "Delete a role", description = "Removes a role from the system by its UUID.")
  @ApiResponse(responseCode = "202", description = "Role deleted successfully")
  @DeleteMapping(path = "/{uuid}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void deleteRole(@PathVariable UUID uuid) {
    log.debug("Controller updateRole method called");
    roleService.deleteRole(uuid);
  }
}
