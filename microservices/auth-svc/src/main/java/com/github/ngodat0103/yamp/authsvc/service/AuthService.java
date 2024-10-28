package com.github.ngodat0103.yamp.authsvc.service;

import com.github.ngodat0103.yamp.authsvc.dto.kafka.SiteUserTopicDto;

public interface AuthService {
  void createUser(SiteUserTopicDto siteUserTopicDto);

  void updateUser(SiteUserTopicDto siteUserTopicDto);

  void deleteUser(SiteUserTopicDto siteUserTopicDto);
}
