package com.example.yamp.usersvc.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
public class UserAddressId implements Serializable {
  @Serial private static final long serialVersionUID = -4007745540022246040L;

  @NotNull
  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @NotNull
  @Column(name = "address_id", nullable = false)
  private UUID addressId;

  public UserAddressId() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserAddressId entity = (UserAddressId) o;
    return Objects.equals(this.userId, entity.userId)
        && Objects.equals(this.addressId, entity.addressId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, addressId);
  }
}
