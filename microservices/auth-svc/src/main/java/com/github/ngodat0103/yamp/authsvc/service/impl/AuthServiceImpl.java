package com.github.ngodat0103.yamp.authsvc.service.impl;

import com.github.ngodat0103.yamp.authsvc.dto.kafka.SiteUserTopicDto;
import com.github.ngodat0103.yamp.authsvc.dto.mapper.UserMapper;
import com.github.ngodat0103.yamp.authsvc.persistence.entity.Role;
import com.github.ngodat0103.yamp.authsvc.persistence.repository.RoleRepository;
import com.github.ngodat0103.yamp.authsvc.persistence.repository.UserRepository;
import com.github.ngodat0103.yamp.authsvc.service.AuthService;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RoleRepository roleRepository;

  private static final String DEFAULT_ROLE = "CUSTOMER";

  @Override
  public void createUser(SiteUserTopicDto siteUserTopicDto) {
    if (!userRepository.existsByemailAddress(siteUserTopicDto.emailAddress())) {
      var user = userMapper.mapToEntity(siteUserTopicDto);
      Role defaultRole =
          roleRepository
              .findRoleByRoleName(DEFAULT_ROLE)
              .orElseGet(
                  () -> {
                    if (log.isDebugEnabled()) {
                      log.debug("Role: {} not found", DEFAULT_ROLE);
                      log.debug("Creating default role: {}", DEFAULT_ROLE);
                    }
                    var role = new Role();
                    role.setRoleName(DEFAULT_ROLE);
                    return roleRepository.save(role);
                  });
      user.setRole(defaultRole);

      userRepository.save(user);
      if (log.isDebugEnabled()) {
        log.debug("User: {} saved", user);
      }
    }
  }

  @Override
  public void updateUser(SiteUserTopicDto siteUserTopicDto) {
    if (checkemailAddressExists(siteUserTopicDto.emailAddress())) {
      var user = userMapper.mapToEntity(siteUserTopicDto);
      user.setEmailAddress(siteUserTopicDto.emailAddress());
      user.setHashedPassword(siteUserTopicDto.emailAddress());
      userRepository.save(user);
      if (log.isDebugEnabled()) {
        log.debug("UserId: {} updated", user.getId());
      }
    }
  }

  @Override
  public void deleteUser(SiteUserTopicDto siteUserTopicDto) {
    checkemailAddressExists(siteUserTopicDto.emailAddress());
    var user = userMapper.mapToEntity(siteUserTopicDto);
    userRepository.delete(user);
  }

  private boolean checkemailAddressExists(String emailAddress) {
    boolean result = userRepository.existsByemailAddress(emailAddress);
    if (log.isDebugEnabled() && !result) {
      log.debug("emailAddress: {} not found", emailAddress);
    }
    return result;
  }
}
