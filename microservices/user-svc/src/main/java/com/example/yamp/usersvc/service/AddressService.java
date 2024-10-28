package com.example.yamp.usersvc.service;

import com.example.yamp.usersvc.dto.AddressDto;
import java.util.List;
import java.util.UUID;

public interface AddressService {
  AddressDto createAddress(AddressDto addressDto);

  AddressDto getAddressById(UUID id);

  List<AddressDto> getAllAddresses();

  AddressDto updateAddress(AddressDto addressDto);

  void deleteAddress(UUID id);
}
