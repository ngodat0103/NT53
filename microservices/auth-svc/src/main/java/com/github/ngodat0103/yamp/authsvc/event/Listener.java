package com.github.ngodat0103.yamp.authsvc.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ngodat0103.yamp.authsvc.dto.kafka.Action;
import com.github.ngodat0103.yamp.authsvc.dto.kafka.SiteUserTopicDto;
import com.github.ngodat0103.yamp.authsvc.service.AuthService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@KafkaListener(topics = "user-svc-topic")
@Service
@AllArgsConstructor
@Slf4j
public class Listener {

  private final AuthService authService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaHandler(isDefault = true)
  private void authsvcHandler(ConsumerRecord<UUID, String> message) throws JsonProcessingException {
    if (log.isDebugEnabled()) {
      log.debug("Received message: {}", message.value());
    }

    UUID id = message.key();
    var siteUserTopicDto = objectMapper.readValue(message.value(), SiteUserTopicDto.class);
    Action action = siteUserTopicDto.action();

    if (log.isDebugEnabled()) {
      log.debug("Doing action: {} with {}}", action, id);
    }
    if (action == Action.CREATE) {
      authService.createUser(siteUserTopicDto);
    } else if (action == Action.UPDATE) {
      authService.updateUser(siteUserTopicDto);
    } else if (action == Action.DELETE) {
      authService.deleteUser(siteUserTopicDto);
    }
  }
}
