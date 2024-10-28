package com.github.ngodat0103.yamp.authsvc.service;

import com.github.ngodat0103.yamp.authsvc.dto.RoleDto;
import java.util.Set;
import java.util.UUID;

public interface RoleService {

  Set<RoleDto> getRole();

  void addRole(RoleDto roleDto);

  void updateRole(UUID uuid, RoleDto roleDto);

  void deleteRole(UUID uuid);
}
