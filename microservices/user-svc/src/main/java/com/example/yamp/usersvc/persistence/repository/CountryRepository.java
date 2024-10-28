package com.example.yamp.usersvc.persistence.repository;

import com.example.yamp.usersvc.persistence.entity.Country;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, UUID> {
  boolean existsByCountryNameAndIdNot(String countryName, UUID id);

  boolean existsByCountryName(String countryName);
}
