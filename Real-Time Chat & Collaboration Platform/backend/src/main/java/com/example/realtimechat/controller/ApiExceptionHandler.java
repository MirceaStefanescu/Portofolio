package com.example.realtimechat.controller;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
    ErrorResponse response = new ErrorResponse("BAD_REQUEST", ex.getMessage(), Instant.now().toString());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  public record ErrorResponse(String code, String message, String timestamp) {
  }
}
