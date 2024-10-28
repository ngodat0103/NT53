package com.example.yamp.usersvc.exception;

import java.util.UUID;
import java.util.function.Supplier;
import org.slf4j.Logger;

public class Util {

  public static Supplier<NotFoundException> customerNotFoundExceptionSupplier(
      Logger log, UUID customerUuid) {
    return () -> {
      log.debug("Customer not found for UUID: {}", customerUuid);
      return new NotFoundException("Customer not found for UUID: " + customerUuid);
    };
  }

  public static Supplier<NotFoundException> addressNotFoundExceptionSupplier(
      Logger log, UUID addressUuid) {
    return () -> {
      log.debug("Address not found for UUID: {}", addressUuid);
      return new NotFoundException("Address not found for UUID: " + addressUuid);
    };
  }
}
