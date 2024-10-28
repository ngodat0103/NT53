package com.example.yamp.usersvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private UUID id;
  @Size(max = 10)
  private String unitNumber;

  @Size(max = 10)
  private String streetNumber;

  @Size(max = 255)
  private String addressLine1;

  @Size(max = 255)
  private String addressLine2;

  @Size(max = 100)
  private String city;

  @Size(max = 100)
  private String region;

  @Size(max = 20)
  private String postalCode;

  private UUID countryId;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)

  private LocalDateTime createdAt;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)

  private LocalDateTime lastModifiedAt;
}
