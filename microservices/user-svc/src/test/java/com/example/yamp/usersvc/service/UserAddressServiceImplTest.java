package com.example.yamp.usersvc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.yamp.usersvc.dto.UserAddressDto;
import com.example.yamp.usersvc.dto.mapper.UserAddressMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.persistence.entity.UserAddress;
import com.example.yamp.usersvc.persistence.entity.UserAddressId;
import com.example.yamp.usersvc.persistence.repository.UserAddressRepository;
import com.example.yamp.usersvc.service.impl.UserAddressServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserAddressServiceImplTest {

  @Mock private UserAddressRepository userAddressRepository;

  @Mock private UserAddressMapper userAddressMapper;

  @InjectMocks private UserAddressServiceImpl userAddressService;

  private UserAddressDto userAddressDto;
  private UserAddress userAddress;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userAddressDto =
        UserAddressDto.builder().userId(UUID.randomUUID()).addressId(UUID.randomUUID()).build();

    userAddress = new UserAddress();
    userAddress.setId(new UserAddressId(userAddressDto.getUserId(), userAddressDto.getAddressId()));
  }

  @Test
  void createUserAddress_shouldThrowConflictException_whenUserAddressExists() {
    when(userAddressRepository.existsById(any(UserAddressId.class))).thenReturn(true);
    when(userAddressMapper.toEntity(any(UserAddressDto.class))).thenReturn(userAddress);
    assertThrows(
        ConflictException.class, () -> userAddressService.createUserAddress(userAddressDto));
  }

  @Test
  void createUserAddress_shouldReturnUserAddressDto_whenUserAddressDoesNotExist() {
    when(userAddressRepository.existsById(any(UserAddressId.class))).thenReturn(false);
    when(userAddressMapper.toEntity(any(UserAddressDto.class))).thenReturn(userAddress);
    when(userAddressRepository.save(any(UserAddress.class))).thenReturn(userAddress);
    when(userAddressMapper.toDto(any(UserAddress.class))).thenReturn(userAddressDto);

    UserAddressDto result = userAddressService.createUserAddress(userAddressDto);

    assertNotNull(result);
    verify(userAddressRepository, times(1)).save(any(UserAddress.class));
  }

  @Test
  void getUserAddressById_shouldReturnUserAddressDto_whenUserAddressExists() {
    when(userAddressRepository.findById(any(UserAddressId.class)))
        .thenReturn(Optional.of(userAddress));
    when(userAddressMapper.toDto(any(UserAddress.class))).thenReturn(userAddressDto);

    UserAddressDto result =
        userAddressService.getUserAddressById(UUID.randomUUID(), UUID.randomUUID());

    assertNotNull(result);
  }

  @Test
  void getAllUserAddresses_shouldReturnListOfUserAddressDto() {
    when(userAddressRepository.findAll()).thenReturn(List.of(userAddress));
    when(userAddressMapper.toDto(any(UserAddress.class))).thenReturn(userAddressDto);

    List<UserAddressDto> result = userAddressService.getAllUserAddresses();

    assertFalse(result.isEmpty());
  }

  @Test
  void deleteUserAddress_shouldInvokeRepositoryDeleteById() {
    UUID userId = UUID.randomUUID();
    UUID addressId = UUID.randomUUID();
    userAddressService.deleteUserAddress(userId, addressId);
    verify(userAddressRepository, times(1)).deleteById(new UserAddressId(userId, addressId));
  }
}
