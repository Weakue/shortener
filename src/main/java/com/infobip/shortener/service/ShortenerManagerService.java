package com.infobip.shortener.service;

import com.infobip.shortener.dao.AccountsDao;
import com.infobip.shortener.exception.AccountAlreadyExistedException;

import lombok.val;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class ShortenerManagerService {

  @Value("app.password.length:8")
  private Integer passwordLength;

  @Autowired
  @Qualifier("sha-256hasher")
  MessageDigest hasher;

  @Autowired
  AccountsDao accountsDao;

  public AccountResponse createAccount(String accountId){
    val password = generatePassword();
    try {
      accountsDao.createAccount(accountId, new String(hasher.digest(password.getBytes())));
    } catch (AccountAlreadyExistedException ex) {
      return AccountResponse.builder()
          .description("Account already exists")
          .build();
    }
    return AccountResponse.builder()
        .description("Account created successfully")
        .success(true)
        .password(password)
        .build();
  }

  private String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(passwordLength);
  }


}
