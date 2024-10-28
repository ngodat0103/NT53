package com.example.yamp.usersvc.persistence.repository;

import com.example.yamp.usersvc.persistence.entity.UserAddress;
import com.example.yamp.usersvc.persistence.entity.UserAddressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, UserAddressId> {}
