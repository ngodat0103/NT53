package com.example.yamp.usersvc.dto.mapper;

import com.example.yamp.usersvc.dto.CountryDto;
import com.example.yamp.usersvc.persistence.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CountryMapper {

  CountryDto toDto(Country country);

  Country toEntity(CountryDto countryDto);
}
