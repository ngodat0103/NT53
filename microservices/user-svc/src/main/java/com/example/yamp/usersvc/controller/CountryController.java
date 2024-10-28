package com.example.yamp.usersvc.controller;

import com.example.yamp.usersvc.dto.CountryDto;
import com.example.yamp.usersvc.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
@Tag(name = "Country", description = "The Country API")
public class CountryController {

  private final CountryService countryService;

  @PostMapping
  @Operation(
      summary = "Create a new country",
      description = "Creates a new country and returns the created country.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Country created successfully"),
        @ApiResponse(responseCode = "409", description = "Country already exists")
      })
  public ResponseEntity<CountryDto> createCountry(@RequestBody @Valid CountryDto countryDto) {
    CountryDto createdCountry = countryService.createCountry(countryDto);
    return ResponseEntity.status(201).body(createdCountry);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get country by ID", description = "Retrieves a country by its ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Country found"),
        @ApiResponse(responseCode = "404", description = "Country not found")
      })
  public ResponseEntity<CountryDto> getCountryById(@PathVariable UUID id) {
    CountryDto country = countryService.getCountryById(id);
    return country != null ? ResponseEntity.ok(country) : ResponseEntity.notFound().build();
  }

  @GetMapping
  @Operation(summary = "Get all countries", description = "Retrieves all countries.")
  @ApiResponse(responseCode = "200", description = "Countries retrieved successfully")
  public ResponseEntity<List<CountryDto>> getAllCountries() {
    List<CountryDto> countries = countryService.getAllCountries();
    return ResponseEntity.ok(countries);
  }

  @PutMapping
  @Operation(
      summary = "Update a country",
      description = "Updates an existing country and returns the updated country.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Country updated successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found")
      })
  public ResponseEntity<CountryDto> updateCountry(@RequestBody CountryDto countryDto) {
    CountryDto updatedCountry = countryService.updateCountry(countryDto);
    return ResponseEntity.ok(updatedCountry);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a country", description = "Deletes a country by its ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Country deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found")
      })
  public ResponseEntity<Void> deleteCountry(@PathVariable UUID id) {
    countryService.deleteCountry(id);
    return ResponseEntity.noContent().build();
  }
}
