package com.github.ngodat0103.yamp.authsvc.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String emailAddress;

  @Column(nullable = false)
  private String hashedPassword;

  @JsonIgnore private boolean enabled = true;
  @JsonIgnore private boolean accountNonExpired = true;
  @JsonIgnore private boolean accountNonLocked = true;
  @JsonIgnore private boolean credentialsNonExpired = true;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_uuid")
  private Role role;

  public User(String emailAddress, String hashedPassword) {
    this.hashedPassword = hashedPassword;
    this.emailAddress = emailAddress;
  }
}
