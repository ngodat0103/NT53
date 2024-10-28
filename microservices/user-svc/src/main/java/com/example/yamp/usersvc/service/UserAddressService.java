package com.example.yamp.usersvc.service;

import com.example.yamp.usersvc.dto.UserAddressDto;
import java.util.List;
import java.util.UUID;

public interface UserAddressService {
  UserAddressDto createUserAddress(UserAddressDto userAddress);

  UserAddressDto getUserAddressById(UUID userId, UUID addressId);

  List<UserAddressDto> getAllUserAddresses();

  UserAddressDto updateUserAddress(UserAddressDto userAddress);

  void deleteUserAddress(UUID userId, UUID addressId);
}
