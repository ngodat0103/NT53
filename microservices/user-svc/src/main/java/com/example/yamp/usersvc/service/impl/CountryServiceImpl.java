package com.example.yamp.usersvc.service.impl;

import com.example.yamp.usersvc.dto.CountryDto;
import com.example.yamp.usersvc.dto.mapper.CountryMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.exception.NotFoundException;
import com.example.yamp.usersvc.persistence.entity.Country;
import com.example.yamp.usersvc.persistence.repository.CountryRepository;
import com.example.yamp.usersvc.service.CountryService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

  private CountryRepository countryRepository;

  private CountryMapper countryMapper;

  @Override
  public CountryDto createCountry(CountryDto countryDto) {

    // Check for conflicts
    if (countryRepository.existsByCountryName(countryDto.getCountryName())) {
      throw new ConflictException("Country already exists.");
    }

    Country country = countryMapper.toEntity(countryDto);
    country.setCreatedAt(LocalDateTime.now());
    country.setLastModifiedAt(LocalDateTime.now());
    Country savedCountry = countryRepository.save(country);
    return countryMapper.toDto(savedCountry);
  }

  @Override
  public CountryDto getCountryById(UUID id) {
    Country country = countryRepository.findById(id).orElse(null);
    return country != null ? countryMapper.toDto(country) : null;
  }

  @Override
  public List<CountryDto> getAllCountries() {
    List<Country> countries = countryRepository.findAll();
    return countries.stream().map(countryMapper::toDto).toList();
  }

  @Override
  public CountryDto updateCountry(CountryDto countryDto) {

    // Check for conflicts
    if (countryRepository.existsByCountryNameAndIdNot(
        countryDto.getCountryName(), countryDto.getId())) {
      throw new ConflictException("Country already exists.");
    }
    Country country =
        countryRepository
            .findById(countryDto.getId())
            .orElseThrow(() -> new NotFoundException("Country not found."));

    country.setCountryName(countryDto.getCountryName());
    country.setLastModifiedAt(LocalDateTime.now());
    Country updatedCountry = countryRepository.save(country);
    return countryMapper.toDto(updatedCountry);
  }

  @Override
  public void deleteCountry(UUID id) {
    countryRepository.deleteById(id);
  }
}
