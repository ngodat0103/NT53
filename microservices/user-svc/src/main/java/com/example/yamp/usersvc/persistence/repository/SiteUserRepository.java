package com.example.yamp.usersvc.persistence.repository;

import com.example.yamp.usersvc.persistence.entity.SiteUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, UUID> {
  boolean existsByEmailAddressAndIdNot(String emailAddress, UUID id);
  boolean existsByEmailAddress(String emailAddress);

  boolean existsByPhoneNumber(String phoneNumber);
}
