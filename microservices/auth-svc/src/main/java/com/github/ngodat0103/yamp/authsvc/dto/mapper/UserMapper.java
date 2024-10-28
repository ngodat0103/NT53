package com.github.ngodat0103.yamp.authsvc.dto.mapper;

import com.github.ngodat0103.yamp.authsvc.dto.kafka.SiteUserTopicDto;
import com.github.ngodat0103.yamp.authsvc.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  User mapToEntity(SiteUserTopicDto siteUserTopicDto);
}
