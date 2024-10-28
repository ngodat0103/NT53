package com.example.yamp.usersvc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.yamp.usersvc.dto.AddressDto;
import com.example.yamp.usersvc.dto.mapper.AddressMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.persistence.entity.Address;
import com.example.yamp.usersvc.persistence.repository.AddressRepository;
import com.example.yamp.usersvc.service.impl.AddressServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AddressServiceImplTest {

  @Mock private AddressRepository addressRepository;

  @Mock private AddressMapper addressMapper;

  @InjectMocks private AddressServiceImpl addressService;

  private AddressDto addressDto;
  private Address address;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    addressDto =
        AddressDto.builder()
            .id(UUID.randomUUID())
            .unitNumber("123")
            .streetNumber("123")
            .addressLine1("Main St")
            .city("CityName")
            .build();
    address = new Address();
    address.setId(UUID.randomUUID());
  }

  @Test
  void createAddress_shouldThrowConflictException_whenAddressExists() {
    when(addressRepository.existsByStreetNumberAndAddressLine1AndCity(
            anyString(), anyString(), anyString()))
        .thenReturn(true);

    assertThrows(ConflictException.class, () -> addressService.createAddress(addressDto));
  }

  @Test
  void createAddress_shouldReturnAddressDto_whenAddressDoesNotExist() {
    when(addressRepository.existsByStreetNumberAndAddressLine1AndCity(
            anyString(), anyString(), anyString()))
        .thenReturn(false);
    when(addressMapper.toEntity(any(AddressDto.class))).thenReturn(address);
    when(addressRepository.save(any(Address.class))).thenReturn(address);
    when(addressMapper.toDto(any(Address.class))).thenReturn(addressDto);

    AddressDto result = addressService.createAddress(addressDto);

    assertNotNull(result);
    verify(addressRepository, times(1)).save(any(Address.class));
  }

  @Test
  void getAddressById_shouldReturnAddressDto_whenAddressExists() {
    when(addressRepository.findById(any(UUID.class))).thenReturn(Optional.of(address));
    when(addressMapper.toDto(any(Address.class))).thenReturn(addressDto);

    AddressDto result = addressService.getAddressById(UUID.randomUUID());

    assertNotNull(result);
  }

  @Test
  void getAllAddresses_shouldReturnListOfAddressDto() {
    when(addressRepository.findAll()).thenReturn(List.of(address));
    when(addressMapper.toDto(any(Address.class))).thenReturn(addressDto);

    List<AddressDto> result = addressService.getAllAddresses();

    assertFalse(result.isEmpty());
  }

  @Test
  void updateAddress_shouldThrowConflictException_whenAddressExists() {
    when(addressRepository.existsByStreetNumberAndAddressLine1AndCityAndIdNot(
            anyString(), anyString(), anyString(), any(UUID.class)))
        .thenReturn(true);

    assertThrows(ConflictException.class, () -> addressService.updateAddress(addressDto));
  }

  @Test
  void deleteAddress_shouldInvokeRepositoryDeleteById() {
    UUID id = UUID.randomUUID();
    addressService.deleteAddress(id);
    verify(addressRepository, times(1)).deleteById(id);
  }
}
