package com.example.yamp.usersvc.dto.mapper;

import com.example.yamp.usersvc.dto.AddressDto;
import com.example.yamp.usersvc.persistence.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AddressMapper {

  @Mapping(source = "country.id", target = "countryId")
  AddressDto toDto(Address address);

  @Mapping(source = "countryId", target = "country.id")
  Address toEntity(AddressDto addressDto);
}
