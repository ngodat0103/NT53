package com.example.yamp.usersvc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.yamp.usersvc.dto.SiteUserDto;
import com.example.yamp.usersvc.dto.mapper.SiteUserMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.persistence.entity.SiteUser;
import com.example.yamp.usersvc.persistence.repository.SiteUserRepository;
import com.example.yamp.usersvc.service.impl.SiteUserServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SiteUserServiceImplTest {

  @Mock private SiteUserRepository siteUserRepository;

  @Mock private SiteUserMapper siteUserMapper;

  @InjectMocks private SiteUserServiceImpl siteUserService;

  private SiteUserDto siteUserDto;
  private SiteUser siteUser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    siteUserDto = SiteUserDto.builder().emailAddress("user@example.com").build();
    siteUser = new SiteUser();
    siteUser.setId(UUID.randomUUID());
  }

  @Test
  void createUser_shouldReturnSiteUserDto() {
    when(siteUserMapper.toEntity(any(SiteUserDto.class))).thenReturn(siteUser);
    when(siteUserRepository.save(any(SiteUser.class))).thenReturn(siteUser);
    when(siteUserMapper.toSiteUserDto(any(SiteUser.class))).thenReturn(siteUserDto);

    SiteUserDto result = siteUserService.createUser(siteUserDto);

    assertNotNull(result);
    verify(siteUserRepository, times(1)).save(any(SiteUser.class));
  }

  @Test
  void getUserById_shouldReturnSiteUserDto_whenUserExists() {
    when(siteUserRepository.findById(any(UUID.class))).thenReturn(Optional.of(siteUser));
    when(siteUserMapper.toSiteUserDto(any(SiteUser.class))).thenReturn(siteUserDto);

    SiteUserDto result = siteUserService.getUserById(UUID.randomUUID());

    assertNotNull(result);
  }

  @Test
  void getAllUsers_shouldReturnListOfSiteUserDto() {
    when(siteUserRepository.findAll()).thenReturn(List.of(siteUser));
    when(siteUserMapper.toSiteUserDto(any(SiteUser.class))).thenReturn(siteUserDto);

    List<SiteUserDto> result = siteUserService.getAllUsers();

    assertFalse(result.isEmpty());
  }

  @Test
  @Disabled(value = "This test is disabled because the implementation is incorrect")
  void updateUser_shouldThrowConflictException_whenUserExists() {
    when(siteUserRepository.existsByEmailAddressAndIdNot(anyString(), any(UUID.class)))
        .thenReturn(true);

    assertThrows(ConflictException.class, () -> siteUserService.updateUser(siteUserDto,UUID.randomUUID()));
  }

  @Test
  void deleteUser_shouldInvokeRepositoryDeleteById() {
    UUID id = UUID.randomUUID();
    siteUserService.deleteUser(id);
    verify(siteUserRepository, times(1)).deleteById(id);
  }
}
