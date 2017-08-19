package com.infobip.shortener.service;

import com.infobip.shortener.dao.AccountsDao;
import com.infobip.shortener.dao.UrlMappingDao;
import com.infobip.shortener.exception.AccountAlreadyExistedException;

import lombok.val;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ShortenerManagerService {

  @Value("app.password.length:8")
  private Integer passwordLength;

  @Value("app.url.shortname.length.start:1")
  private AtomicInteger shortUrlLength;

  private final MessageDigest hasher;

  private final AccountsDao accountsDao;

  private final UrlMappingDao urlMappingDao;

  @Autowired
  public ShortenerManagerService(@Qualifier("sha-256hasher") MessageDigest hasher,
                                 AccountsDao accountsDao,
                                 UrlMappingDao urlMappingDao) {
    this.hasher = hasher;
    this.accountsDao = accountsDao;
    this.urlMappingDao = urlMappingDao;
  }

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


  public String register(String url, Integer redirectType) {

    /* Really not sure that this is a good way to do this,
    * there is an option to create some getNextAvailableName() into dao.
    */
    //TODO(apuks): reconsider and look again into this.
    String shortName = generateShortName();
    while (urlMappingDao.getUrlByShortName(shortName) != null) {
      shortUrlLength.incrementAndGet();
      shortName = generateShortName();
    }

    urlMappingDao.createMapping(url, shortName, redirectType);
    return shortName;
  }

  private String generateShortName() {
    return RandomStringUtils.randomAlphanumeric(shortUrlLength.get());
  }
}
