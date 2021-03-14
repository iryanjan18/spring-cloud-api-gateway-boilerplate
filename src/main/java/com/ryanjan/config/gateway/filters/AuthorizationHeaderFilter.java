package com.ryanjan.config.gateway.filters;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.ryanjan.config.gateway.JwtSecretKey;
import com.ryanjan.config.gateway.exception.GatewayRequestException;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created on 2/20/21
 * Created by Ryan Jan Borja
 * rrborja@region1.dost.gov.ph
 * ryanjan18@outlook.ph
 */
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

  private final JwtSecretKey jwtSecretKey;
  private final Environment environment;

  @Autowired
  public AuthorizationHeaderFilter(JwtSecretKey jwtSecretKey, Environment environment) {
    super(Config.class);
    this.jwtSecretKey = jwtSecretKey;
    this.environment = environment;
  }

  public static class Config {
  }

  @Override
  public GatewayFilter apply(Config config) {
    return ((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return onError(exchange, "Not Authorized!");
      }

      String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String token = authorizationHeader.replace("Bearer", "");

      try {
        if (!isJwtValid(token)) {
          return onError(exchange, "Request Not Authorized!");
        }
      } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
        e.printStackTrace();
      }

      return chain.filter(exchange);
    });
  }

  private Mono<Void> onError(ServerWebExchange exchange, String message) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    return response.setComplete();
  }

  private boolean isJwtValid(String token) throws InvalidKeySpecException,
    NoSuchAlgorithmException,
    SignatureException,
    MalformedJwtException {
    boolean result = true;
    Jws<Claims> claimsJws;
    String subject;
    String issuer;

    try {
      claimsJws = Jwts
        .parser()
        .setSigningKey(jwtSecretKey.getVerifyingKey())
        .parseClaimsJws(token);

      Claims body = claimsJws.getBody();
      subject = body.getSubject();
      issuer = body.getIssuer();
    } catch (Exception e) {
      throw new GatewayRequestException("Invalid request!");
    }

    if (subject == null || subject.isEmpty()) {
      result = false;
    } else if (!issuer.equals(environment.getProperty("jwt.issuer"))) {
      result = false;
    }

    return result;
  }

}
