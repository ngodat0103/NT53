package com.github.ngodat0103.yamp.authsvc.service.impl;

import static com.github.ngodat0103.yamp.authsvc.Util.*;

import com.github.ngodat0103.yamp.authsvc.Util;
import com.github.ngodat0103.yamp.authsvc.dto.RoleDto;
import com.github.ngodat0103.yamp.authsvc.dto.mapper.RoleMapper;
import com.github.ngodat0103.yamp.authsvc.persistence.entity.Role;
import com.github.ngodat0103.yamp.authsvc.persistence.repository.RoleRepository;
import com.github.ngodat0103.yamp.authsvc.service.RoleService;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class RoleServiceImpl implements RoleService {
  private RoleRepository roleRepository;
  private RoleMapper roleMapper;

  @Override
  public Set<RoleDto> getRole() {
    log.debug("Fetching all roles");
    return roleRepository.findAll().stream()
        .map(roleMapper::mapToDto)
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public void addRole(RoleDto roleDto) {

    Role role = roleMapper.mapToEntity(roleDto);
    log.debug("Fetching role: {}", roleDto.getRoleName());
    if (roleRepository.existsByRoleName(role.getRoleName())) {
      throwConflictException(log, "Role", "roleName", role.getRoleName());
    }

    roleRepository.save(role);
    log.debug("Role added: {}", roleDto.getRoleName());
  }

  @Override
  public void updateRole(UUID uuid, RoleDto roleDto) {
    Role role =
        roleRepository
            .findById(uuid)
            .orElseThrow(
                Util.notFoundExceptionSupplier(log, "Role", "roleName", roleDto.getRoleName()));
    log.debug("Updating role: {}", roleDto.getRoleName());
    role.setRoleName(roleDto.getRoleName());
    role.setRoleDescription(roleDto.getRoleDescription());
    roleRepository.save(role);
    log.debug("Role updated: {}", roleDto.getRoleName());
  }

  @Override
  public void deleteRole(UUID uuid) {
    Role role =
        roleRepository
            .findById(uuid)
            .orElseThrow(notFoundExceptionSupplier(log, "Role", "uuid", uuid.toString()));
    log.debug("Deleting role: {}", role.getRoleName());
    roleRepository.delete(role);
  }
}
