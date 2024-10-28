package com.example.yamp.usersvc.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "public")
public class Address extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Size(max = 10)
  @Column(name = "unit_number", length = 10)
  private String unitNumber;

  @Size(max = 10)
  @Column(name = "street_number", length = 10)
  private String streetNumber;

  @Size(max = 255)
  @Column(name = "address_line1")
  private String addressLine1;

  @Size(max = 255)
  @Column(name = "address_line2")
  private String addressLine2;

  @Size(max = 100)
  @Column(name = "city", length = 100)
  private String city;

  @Size(max = 100)
  @Column(name = "region", length = 100)
  private String region;

  @Size(max = 20)
  @Column(name = "postal_code", length = 20)
  private String postalCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_id")
  private Country country;
}
