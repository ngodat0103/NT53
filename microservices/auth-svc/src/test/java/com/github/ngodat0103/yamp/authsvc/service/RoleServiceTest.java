package com.github.ngodat0103.yamp.authsvc.service;

import static com.github.ngodat0103.yamp.authsvc.Util.*;
import static org.mockito.BDDMockito.*;

import com.github.ngodat0103.yamp.authsvc.dto.RoleDto;
import com.github.ngodat0103.yamp.authsvc.dto.mapper.RoleMapper;
import com.github.ngodat0103.yamp.authsvc.dto.mapper.RoleMapperImpl;
import com.github.ngodat0103.yamp.authsvc.exception.ConflictException;
import com.github.ngodat0103.yamp.authsvc.exception.NotFoundException;
import com.github.ngodat0103.yamp.authsvc.persistence.entity.Role;
import com.github.ngodat0103.yamp.authsvc.persistence.repository.RoleRepository;
import com.github.ngodat0103.yamp.authsvc.service.impl.RoleServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit-test")
class RoleServiceTest {
  RoleRepository roleRepository;
  RoleService roleService;
  private RoleMapper roleMapper;
  RoleDto roleDto =
      RoleDto.builder().roleName("roleTest").roleDescription("Description of role").build();
  private final String roleName = roleDto.getRoleName();
  private final String roleNameUpperCase = roleName.toUpperCase();

  @BeforeEach
  public void setUp() {
    roleRepository = mock(RoleRepository.class);
    this.roleMapper = new RoleMapperImpl();
    roleService = new RoleServiceImpl(roleRepository, roleMapper);
  }

  @Test
  @DisplayName("Given already have Role when add Role then return Conflict")
  void givenRole_whenAddRole_thenReturnConflict() {
    given(roleRepository.existsByRoleName(roleNameUpperCase)).willReturn(true);
    Assertions.assertThatThrownBy(() -> roleService.addRole(roleDto))
        .isInstanceOf(ConflictException.class)
        .hasMessage(String.format(TEMPLATE_CONFLICT, "Role", "roleName", roleNameUpperCase));
  }

  @Test
  @DisplayName("Given Role when add Role then saved Role")
  void givenNothing_whenAddRole_thenSaveRole() {
    given(roleRepository.existsByRoleName(roleNameUpperCase)).willReturn(false);
    roleService.addRole(roleDto);
    verify(roleRepository).save(any());
  }

  @Test
  @DisplayName("Given roles when get Roles then return Role")
  void givenRole_whenGetRole_thenReturnRole() {
    Role role = roleMapper.mapToEntity(roleDto);
    role.setUuid(UUID.randomUUID());
    given(roleRepository.findAll()).willReturn(List.of(role));
    Set<RoleDto> roleDtos = roleService.getRole();
    Assertions.assertThat(roleDtos).isNotEmpty();
    Assertions.assertThat(roleDtos).hasSize(1);
    roleDtos.forEach(
        roleDto -> {
          Assertions.assertThat(roleDto.getUuid()).isEqualTo(role.getUuid());
          Assertions.assertThat(roleDto.getRoleName()).isEqualTo(role.getRoleName());
          Assertions.assertThat(roleDto.getRoleDescription()).isEqualTo(role.getRoleDescription());
        });
    verify(roleRepository).findAll();
  }

  @Test
  @DisplayName("Given nothing when get Roles then return NotFoundException")
  void givenRole_whenDeleteRole_thenReturnConflict() {
    UUID uuid = UUID.randomUUID();
    given(roleRepository.findById(uuid)).willReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> roleService.deleteRole(uuid))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(TEMPLATE_NOT_FOUND, "Role", "uuid", uuid);
  }

  @Test
  @DisplayName("Given Role when delete Role then delete Role")
  void givenRole_whenDeleteRole_thenDeleteRole() {
    Role role = roleMapper.mapToEntity(roleDto);
    role.setUuid(UUID.randomUUID());
    given(roleRepository.findById(role.getUuid())).willReturn(Optional.of(role));
    roleService.deleteRole(role.getUuid());
    verify(roleRepository).delete(role);
  }

  //    @Test
  //    @DisplayName("Create role when account does not exist")
  //    @WithMockUser(authorities = "ROLE_ADMIN",username = accountUuid)
  //    public void givenRole_whenAddRole_thenReturnFail() {
  //
  // given(roleRepository.findById(UUID.fromString(accountUuid))).willReturn(Optional.empty());
  //        Assertions.assertThatThrownBy(() -> roleService.addRole("test"))
  //                .isInstanceOf(NotFoundException.class)
  //                .hasMessage("AccountUuid not found");
  //    }
}
