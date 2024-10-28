package com.github.ngodat0103.yamp.authsvc.dto.kafka;

import java.util.UUID;

public record SiteUserTopicDto(
        UUID id,
    String firstName,
    String lastName,
    String emailAddress,
    String hashedPassword,
    Action action) {}
