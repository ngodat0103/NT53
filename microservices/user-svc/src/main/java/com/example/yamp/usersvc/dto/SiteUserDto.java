package com.example.yamp.usersvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Builder
@Getter
public class SiteUserDto {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private UUID id;

  @Email
  @Size(max = 50)
  @NotNull
  private String emailAddress;

  @Size(max = 20)
  private String phoneNumber;

    @Size(max = 10,message = "First name must be less than 10 characters")
    @NotNull
  private String firstName;
    @Size(max = 10,message = "Last name must be less than 10 characters")
    @NotNull
  private String lastName;

  @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
  @NotNull
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime createdAt;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime lastModifiedAt;
}
