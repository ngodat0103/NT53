package com.github.ngodat0103.yamp.authsvc.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @Getter
  static class Error {
    private final String detail;
    private final String pointer;
    @JsonIgnore private final String jsonPathSchema = "#/";

    public Error(String detail, String pointer) {
      this.detail = detail;
      this.pointer = jsonPathSchema + pointer;
    }
  }

  @ExceptionHandler(ConflictException.class)
  public ProblemDetail handleApiException(RuntimeException e, HttpServletRequest request) {
    ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problemDetails.setDetail(e.getMessage());
    problemDetails.setType(URI.create("https://problems-registry.smartbear.com/already-exists"));
    problemDetails.setTitle("Already exists");
    problemDetails.setDetail(e.getMessage());
    problemDetails.setInstance(URI.create(request.getServletPath()));
    return problemDetails;
  }

  @ExceptionHandler(NotFoundException.class)
  public ProblemDetail handleNotFoundException(NotFoundException e, HttpServletRequest request) {
    ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetails.setDetail(e.getMessage());
    problemDetails.setType(URI.create("https://problems-registry.smartbear.com/not-found"));
    problemDetails.setTitle("Not found");
    problemDetails.setInstance(URI.create(request.getServletPath()));
    return problemDetails;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetail handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetails.setType(
        URI.create("https://problems-registry.smartbear.com/missing-body-property"));
    problemDetails.setDetail("The request is not valid.");
    problemDetails.setTitle("Validation Error.");
    problemDetails.setInstance(URI.create(request.getServletPath()));
    Set<Error> errors = new HashSet<>();
    e.getFieldErrors()
        .forEach(
            fieldError -> {
              Error error = new Error(fieldError.getDefaultMessage(), fieldError.getField());
              errors.add(error);
            });
    problemDetails.setProperties(Collections.singletonMap("errors", errors));
    return problemDetails;
  }

  @ExceptionHandler(PSQLException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ProblemDetail handlePSQLException(PSQLException e, HttpServletRequest request) {
    String detail = Objects.requireNonNull(e.getServerErrorMessage()).getDetail();
    assert detail != null;
    if (detail.contains("referenced")) {
      ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
      problemDetails.setDetail(detail.replace("table", "entity"));
      problemDetails.setType(
          URI.create("https://problems-registry.smartbear.com/business-rule-violation"));
      problemDetails.setTitle("Business Rule Violation");
      problemDetails.setInstance(URI.create(request.getServletPath()));
      return problemDetails;
    } else {
      throw new RuntimeException(e);
    }
  }
}
