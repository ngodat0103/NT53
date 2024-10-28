package com.example.yamp.usersvc.dto.mapper;

import com.example.yamp.usersvc.dto.SiteUserDto;
import com.example.yamp.usersvc.dto.kafka.Action;
import com.example.yamp.usersvc.dto.kafka.SiteUserTopicDto;
import com.example.yamp.usersvc.persistence.entity.SiteUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface SiteUserMapper {

  SiteUserDto toSiteUserDto(SiteUser siteUser);
  SiteUserTopicDto toSiteUserTopicDto(SiteUser siteUser, String hashedPassword, Action action);

  SiteUser toEntity(SiteUserDto siteUserDto);
}
