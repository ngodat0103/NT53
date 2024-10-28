package com.github.ngodat0103.yamp.authsvc.persistence.entity;

import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false, updatable = false)
  private UUID uuid;

  @Column(nullable = false, unique = true)
  private String roleName;

  private String roleDescription;

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private Set<User> users;
}
