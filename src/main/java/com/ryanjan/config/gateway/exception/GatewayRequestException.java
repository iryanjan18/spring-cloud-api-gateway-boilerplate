package com.ryanjan.config.gateway.exception;

/**
 * Created on 2/24/21
 * Created by Ryan Jan Borja
 * rrborja@region1.dost.gov.ph
 * ryanjan18@outlook.ph
 */

public class GatewayRequestException extends RuntimeException {

  private static final long serialVersionUID = 7177237459828797776L;

  public GatewayRequestException(String message) {
    super(message);
  }

  public GatewayRequestException(String message, Throwable cause) {
    super(message, cause);
  }

}
