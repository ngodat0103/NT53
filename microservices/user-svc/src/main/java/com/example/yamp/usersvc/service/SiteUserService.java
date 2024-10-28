package com.example.yamp.usersvc.service;

import com.example.yamp.usersvc.dto.SiteUserDto;
import java.util.List;
import java.util.UUID;

public interface SiteUserService {
  SiteUserDto createUser(SiteUserDto user);

  SiteUserDto getUserById(UUID id);

  List<SiteUserDto> getAllUsers();

  SiteUserDto updateUser(SiteUserDto user, UUID id);

  void deleteUser(UUID id);
}
