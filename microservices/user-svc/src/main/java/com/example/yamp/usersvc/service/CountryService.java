package com.example.yamp.usersvc.service;

import com.example.yamp.usersvc.dto.CountryDto;
import java.util.List;
import java.util.UUID;

public interface CountryService {
  CountryDto createCountry(CountryDto country);

  CountryDto getCountryById(UUID id);

  List<CountryDto> getAllCountries();

  CountryDto updateCountry(CountryDto country);

  void deleteCountry(UUID id);
}
