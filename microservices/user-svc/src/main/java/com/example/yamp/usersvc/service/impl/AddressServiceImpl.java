package com.example.yamp.usersvc.service.impl;

import com.example.yamp.usersvc.dto.AddressDto;
import com.example.yamp.usersvc.dto.mapper.AddressMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.exception.NotFoundException;
import com.example.yamp.usersvc.persistence.entity.Address;
import com.example.yamp.usersvc.persistence.entity.Country;
import com.example.yamp.usersvc.persistence.repository.AddressRepository;
import com.example.yamp.usersvc.persistence.repository.CountryRepository;
import com.example.yamp.usersvc.service.AddressService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final CountryRepository countryRepository;
  private AddressRepository addressRepository;

  private AddressMapper addressMapper;

  @Override
  public AddressDto createAddress(AddressDto addressDto) {

    if (addressRepository.existsByStreetNumberAndAddressLine1AndCity(
        addressDto.getStreetNumber(), addressDto.getAddressLine1(), addressDto.getCity())) {
      throw new ConflictException("Address already exists.");
    }

    Address address = addressMapper.toEntity(addressDto);
    address.setLastModifiedAt(LocalDateTime.now());
    address.setCreatedAt(LocalDateTime.now());
    Address savedAddress = addressRepository.save(address);

    return addressMapper.toDto(savedAddress);
  }

  @Override
  public AddressDto getAddressById(UUID id) {
    Address address = addressRepository.findById(id).orElse(null);
    return address != null ? addressMapper.toDto(address) : null;
  }

  @Override
  public List<AddressDto> getAllAddresses() {
    List<Address> addresses = addressRepository.findAll();
    return addresses.stream().map(addressMapper::toDto).toList();
  }

  @Override
  public AddressDto updateAddress(AddressDto addressDto) {

    // Check for conflicts
    if (addressRepository.existsByStreetNumberAndAddressLine1AndCityAndIdNot(
        addressDto.getStreetNumber(),
        addressDto.getAddressLine1(),
        addressDto.getCity(),
        addressDto.getId())) {
      throw new ConflictException("Address already exists.");
    }

    Address address =
        addressRepository
            .findById(addressDto.getId())
            .orElseThrow(() -> new NotFoundException("Address not found."));

    Country country =
        countryRepository
            .findById(addressDto.getCountryId())
            .orElseThrow(() -> new NotFoundException("Country not found."));
    address.setAddressLine2(addressDto.getAddressLine2());
    address.setCity(addressDto.getCity());
    address.setCountry(country);
    address.setStreetNumber(addressDto.getStreetNumber());
    address.setUnitNumber(addressDto.getUnitNumber());
    address.setPostalCode(addressDto.getPostalCode());
    address.setRegion(addressDto.getRegion());
    address.setLastModifiedAt(LocalDateTime.now());
    return addressMapper.toDto(address);
  }

  @Override
  public void deleteAddress(UUID id) {
    addressRepository.deleteById(id);
  }
}
