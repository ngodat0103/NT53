package com.example.yamp.usersvc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.yamp.usersvc.dto.CountryDto;
import com.example.yamp.usersvc.dto.mapper.CountryMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.persistence.entity.Country;
import com.example.yamp.usersvc.persistence.repository.CountryRepository;
import com.example.yamp.usersvc.service.impl.CountryServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CountryServiceImplTest {

  @Mock private CountryRepository countryRepository;

  @Mock private CountryMapper countryMapper;

  @InjectMocks private CountryServiceImpl countryService;

  private CountryDto countryDto;
  private Country country;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    countryDto = CountryDto.builder().id(UUID.randomUUID()).countryName("CountryName").build();
    country = new Country();
    country.setId(UUID.randomUUID());
  }

  @Test
  void createCountry_shouldThrowConflictException_whenCountryExists() {
    when(countryRepository.existsByCountryName(anyString())).thenReturn(true);

    assertThrows(ConflictException.class, () -> countryService.createCountry(countryDto));
  }

  @Test
  void createCountry_shouldReturnCountryDto_whenCountryDoesNotExist() {
    when(countryRepository.existsByCountryName(anyString())).thenReturn(false);
    when(countryMapper.toEntity(any(CountryDto.class))).thenReturn(country);
    when(countryRepository.save(any(Country.class))).thenReturn(country);
    when(countryMapper.toDto(any(Country.class))).thenReturn(countryDto);

    CountryDto result = countryService.createCountry(countryDto);

    assertNotNull(result);
    verify(countryRepository, times(1)).save(any(Country.class));
  }

  @Test
  void getCountryById_shouldReturnCountryDto_whenCountryExists() {
    when(countryRepository.findById(any(UUID.class))).thenReturn(Optional.of(country));
    when(countryMapper.toDto(any(Country.class))).thenReturn(countryDto);

    CountryDto result = countryService.getCountryById(UUID.randomUUID());

    assertNotNull(result);
  }

  @Test
  void getAllCountries_shouldReturnListOfCountryDto() {
    when(countryRepository.findAll()).thenReturn(List.of(country));
    when(countryMapper.toDto(any(Country.class))).thenReturn(countryDto);

    List<CountryDto> result = countryService.getAllCountries();

    assertFalse(result.isEmpty());
  }

  @Test
  void updateCountry_shouldThrowConflictException_whenCountryExists() {
    when(countryRepository.existsByCountryNameAndIdNot(anyString(), any(UUID.class)))
        .thenReturn(true);

    assertThrows(ConflictException.class, () -> countryService.updateCountry(countryDto));
  }

  @Test
  void deleteCountry_shouldInvokeRepositoryDeleteById() {
    UUID id = UUID.randomUUID();
    countryService.deleteCountry(id);
    verify(countryRepository, times(1)).deleteById(id);
  }
}
