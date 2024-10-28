package com.example.yamp.usersvc.service.impl;

import com.example.yamp.usersvc.dto.UserAddressDto;
import com.example.yamp.usersvc.dto.mapper.UserAddressMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.persistence.entity.UserAddress;
import com.example.yamp.usersvc.persistence.entity.UserAddressId;
import com.example.yamp.usersvc.persistence.repository.UserAddressRepository;
import com.example.yamp.usersvc.service.UserAddressService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

  private UserAddressRepository userAddressRepository;

  private UserAddressMapper userAddressMapper;

  @Override
  public UserAddressDto createUserAddress(UserAddressDto userAddressDto) {

    UserAddress userAddress = userAddressMapper.toEntity(userAddressDto);

    if (userAddressRepository.existsById(userAddress.getId())) {
      throw new ConflictException("User address already exists.");
    }
    UserAddress savedUserAddress = userAddressRepository.save(userAddress);
    return userAddressMapper.toDto(savedUserAddress);
  }

  @Override
  public UserAddressDto getUserAddressById(UUID userId, UUID addressId) {
    UserAddress userAddress =
        userAddressRepository.findById(new UserAddressId(userId, addressId)).orElse(null);
    return userAddress != null ? userAddressMapper.toDto(userAddress) : null;
  }

  @Override
  public List<UserAddressDto> getAllUserAddresses() {
    List<UserAddress> userAddresses = userAddressRepository.findAll();
    return userAddresses.stream().map(userAddressMapper::toDto).toList();
  }

  @Override
  public UserAddressDto updateUserAddress(UserAddressDto userAddressDto) {

    UserAddress userAddress = userAddressMapper.toEntity(userAddressDto);
    UserAddress updatedUserAddress = userAddressRepository.save(userAddress);
    return userAddressMapper.toDto(updatedUserAddress);
  }

  @Override
  public void deleteUserAddress(UUID userId, UUID addressId) {
    userAddressRepository.deleteById(new UserAddressId(userId, addressId));
  }
}
