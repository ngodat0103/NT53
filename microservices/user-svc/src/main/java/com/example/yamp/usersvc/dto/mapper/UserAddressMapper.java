package com.example.yamp.usersvc.dto.mapper;

import com.example.yamp.usersvc.dto.UserAddressDto;
import com.example.yamp.usersvc.persistence.entity.UserAddress;
import com.example.yamp.usersvc.persistence.entity.UserAddressId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UserAddressMapper {

  @Mapping(source = "id.userId", target = "userId")
  @Mapping(source = "id.addressId", target = "addressId")
  UserAddressDto toDto(UserAddress userAddress);

  default UserAddress toEntity(UserAddressDto userAddressDto) {
    UserAddress userAddress = new UserAddress();
    UserAddressId userAddressId =
        new UserAddressId(userAddressDto.getUserId(), userAddressDto.getAddressId());
    userAddress.setId(userAddressId);
    userAddress.setIsDefault(userAddressDto.getIsDefault());
    return userAddress;
  }
}
