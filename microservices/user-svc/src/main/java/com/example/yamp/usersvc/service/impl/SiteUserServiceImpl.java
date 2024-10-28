package com.example.yamp.usersvc.service.impl;

import com.example.yamp.usersvc.dto.SiteUserDto;
import com.example.yamp.usersvc.dto.kafka.Action;
import com.example.yamp.usersvc.dto.kafka.SiteUserTopicDto;
import com.example.yamp.usersvc.dto.mapper.SiteUserMapper;
import com.example.yamp.usersvc.exception.ConflictException;
import com.example.yamp.usersvc.exception.NotFoundException;
import com.example.yamp.usersvc.persistence.entity.SiteUser;
import com.example.yamp.usersvc.persistence.repository.SiteUserRepository;
import com.example.yamp.usersvc.service.SiteUserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SiteUserServiceImpl implements SiteUserService {

  private SiteUserRepository siteUserRepository;

  private SiteUserMapper siteUserMapper;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final KafkaTemplate<UUID, SiteUserTopicDto> kafkaTemplate;
  private final static String TOPIC = "user-svc-topic";

  @Override
  public SiteUserDto createUser(SiteUserDto siteUserDto) {
   checkConflict(siteUserDto);
    SiteUser siteUser = siteUserMapper.toEntity(siteUserDto);
    siteUser.setCreatedAt(LocalDateTime.now());
    siteUser.setLastModifiedAt(LocalDateTime.now());
    SiteUser savedSiteUser = siteUserRepository.save(siteUser);

    String hashedPassword = passwordEncoder.encode(siteUserDto.getPassword());
    SiteUserTopicDto siteUserTopicDto = siteUserMapper.toSiteUserTopicDto(savedSiteUser,hashedPassword, Action.CREATE);
    kafkaTemplate.send(TOPIC,savedSiteUser.getId(),siteUserTopicDto);
    if(log.isDebugEnabled()){
      log.debug("User sent to kafka topic. User: {}",siteUserTopicDto);
    }
    return siteUserMapper.toSiteUserDto(savedSiteUser);
  }

  @Override
  public SiteUserDto getUserById(UUID id) {
    SiteUser siteUser = siteUserRepository.findById(id).orElse(null);
    return siteUser != null ? siteUserMapper.toSiteUserDto(siteUser) : null;
  }

  @Override
  public List<SiteUserDto> getAllUsers() {
    List<SiteUser> siteUsers = siteUserRepository.findAll();
    return siteUsers.stream().map(siteUserMapper::toSiteUserDto).toList();
  }

  @Override
  public SiteUserDto updateUser(SiteUserDto siteUserDto,UUID id) {

    SiteUser siteUser =
        siteUserRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("User with this id does not exist."));
      checkConflict(siteUserDto);
      siteUser.setFirstName(siteUserDto.getFirstName());
    siteUser.setLastName(siteUserDto.getLastName());
    siteUser.setEmailAddress(siteUserDto.getEmailAddress());
    siteUser.setPhoneNumber(siteUserDto.getPhoneNumber());
    siteUser.setLastModifiedAt(LocalDateTime.now());

    SiteUser updatedSiteUser = siteUserRepository.save(siteUser);
    return siteUserMapper.toSiteUserDto(updatedSiteUser);
  }

  @Override
  public void deleteUser(UUID id) {
    siteUserRepository.deleteById(id);
  }
  private void checkConflict(SiteUserDto siteUserDto){
    if(siteUserRepository.existsByEmailAddress(siteUserDto.getEmailAddress())){
      if(log.isDebugEnabled()){
        log.debug("User with this email already exists. Email: {}",siteUserDto.getEmailAddress());
      }
      throw new ConflictException("User with this email already exists.");
    }

    String phoneNumber = siteUserDto.getPhoneNumber();
    if(phoneNumber!=null && siteUserRepository.existsByPhoneNumber(phoneNumber)){
      if(log.isDebugEnabled()){
        log.debug("User with this phone number already exists. Phone number: {}",phoneNumber);
      }
      throw new ConflictException("User with this phone number already exists.");
    }

  }
}
