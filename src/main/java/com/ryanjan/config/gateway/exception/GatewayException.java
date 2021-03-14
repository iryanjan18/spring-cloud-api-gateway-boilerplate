package com.ryanjan.config.gateway.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * Created on 2/22/21
 * Created by Ryan Jan Borja
 * rrborja@region1.dost.gov.ph
 * ryanjan18@outlook.ph
 */
@Getter
@NoArgsConstructor
public class GatewayException {

  private String message;
  private HttpStatus status;
  private ZonedDateTime timestamp;

  public GatewayException(String message, HttpStatus status, ZonedDateTime timestamp) {
    this.message = message;
    this.status = status;
    this.timestamp = timestamp;
  }

}
