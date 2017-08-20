package com.infobip.shortener;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;

@Configuration
public class AppConfig {

  @Value("${app.password.hash.algorithm:SHA-256}")
  String hashAlgorithm;

  @Bean
  @SneakyThrows
  @Qualifier("sha-256hasher")
  MessageDigest digest() {
    return MessageDigest.getInstance(hashAlgorithm);
  }


}
