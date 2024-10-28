package com.github.ngodat0103.yamp.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallBackController {
  @GetMapping("/fallback")
  @ResponseStatus(HttpStatus.TEMPORARY_REDIRECT)
  public Mono<String> fallBack() {
    return Mono.just(
        "An error occurred. Please try again later or contact me at github.com/ngodat0103");
  }
}
