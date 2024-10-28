package com.example.yamp.usersvc.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "site_user", schema = "public")
public class SiteUser extends BaseEntity {
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Size(max = 255)
  @Column(name = "email_address",unique = true)
  private String emailAddress;

  @Size(max = 20)
  @Column(name = "phone_number", length = 20,unique = true)
  private String phoneNumber;

  @Column(name = "first_name", length = 100)
  private String firstName;

  @Column(name = "last_name", length = 100)
  private String lastName;
}
