package com.example.yamp.usersvc.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_address", schema = "public")
@Getter
@Setter
public class UserAddress extends BaseEntity {
  @EmbeddedId private UserAddressId id;

  @MapsId("userId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private SiteUser user;

  @MapsId("addressId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @Column(name = "is_default")
  private Boolean isDefault;
}
