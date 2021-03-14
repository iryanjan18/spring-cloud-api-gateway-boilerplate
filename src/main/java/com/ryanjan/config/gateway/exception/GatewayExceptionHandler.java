package com.ryanjan.config.gateway.exception;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created on 2/22/21
 * Created by Ryan Jan Borja
 * rrborja@region1.dost.gov.ph
 * ryanjan18@outlook.ph
 */
@ControllerAdvice
public class GatewayExceptionHandler {

  private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
  private static final String INVALID_ACCESS_MESSAGE = "Your request is invalid";
  private static final ZonedDateTime CURRENT_DATETIME = ZonedDateTime.now(ZoneId.of("Z"));

  @ExceptionHandler(value = {
    MalformedJwtException.class,
    InvalidKeyException.class,
    SignatureException.class,
    NoSuchAlgorithmException.class
  })
  public ResponseEntity<Object> handleInvalidJwt() {
    GatewayException gatewayException = new GatewayException(INVALID_ACCESS_MESSAGE, BAD_REQUEST, CURRENT_DATETIME);
    return new ResponseEntity<>(gatewayException, BAD_REQUEST);
  }

}
