package com.ryanjan.config.gateway;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created on 2/20/21
 * Created by Ryan Jan Borja
 * rrborja@region1.dost.gov.ph
 * ryanjan18@outlook.ph
 */
@Configuration
@AllArgsConstructor
public class JwtSecretKey {

  private final Environment environment;

  public PublicKey getVerifyingKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
    String rawKey = environment.getProperty("jwt.token");
    String cleanKey = rawKey
      .replaceAll("\\n", "")
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "");

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(cleanKey));

//    byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
//    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//
    return keyFactory.generatePublic(keySpec);
  }

}
