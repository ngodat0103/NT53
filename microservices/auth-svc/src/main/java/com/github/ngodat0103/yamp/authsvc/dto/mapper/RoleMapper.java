package com.github.ngodat0103.yamp.authsvc.dto.mapper;

import com.github.ngodat0103.yamp.authsvc.dto.RoleDto;
import com.github.ngodat0103.yamp.authsvc.persistence.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface RoleMapper {

  RoleDto mapToDto(Role role);

  @Mapping(target = "roleName", expression = "java(roleDto.getRoleName().toUpperCase())")
  Role mapToEntity(RoleDto roleDto);
}
