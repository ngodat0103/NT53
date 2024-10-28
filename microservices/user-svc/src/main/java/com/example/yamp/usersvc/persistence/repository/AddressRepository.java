package com.example.yamp.usersvc.persistence.repository;

import com.example.yamp.usersvc.persistence.entity.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, UUID> {
  boolean existsByStreetNumberAndAddressLine1AndCityAndIdNot(
      String streetNumber, String addressLine1, String city, UUID id);

  boolean existsByStreetNumberAndAddressLine1AndCity(
      String streetNumber, String addressLine1, String city);
}
