package dev.luzifer.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
    log.error("Failed request: {}", request.getDescription(false), ex);
    return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
