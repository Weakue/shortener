package com.infobip.shortener.service;

import com.infobip.shortener.dao.AccountsDao;
import com.infobip.shortener.dao.StatisticsDao;
import com.infobip.shortener.dao.UrlMappingDao;
import com.infobip.shortener.exception.AccountAlreadyExistedException;
import com.infobip.shortener.service.model.Account;
import com.infobip.shortener.service.model.AccountResponse;

import lombok.val;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ShortenerManagerService {

  @Value("${app.password.length:8}")
  private Integer passwordLength;


  //I still dont know a cleaner way to do this.
  @Value("#{new java.util.concurrent.atomic.AtomicInteger("
      + "new Integer('${app.url.shortname.length.start:1}')"
      + ".intValue())}")
  private AtomicInteger shortUrlLength;

  private final MessageDigest hasher;

  private final AccountsDao accountsDao;

  private final UrlMappingDao urlMappingDao;

  private final StatisticsDao statisticsDao;

  @Autowired
  public ShortenerManagerService(@Qualifier("sha-256hasher") MessageDigest hasher,
                                 AccountsDao accountsDao,
                                 UrlMappingDao urlMappingDao,
                                 StatisticsDao statisticsDao) {
    this.hasher = hasher;
    this.accountsDao = accountsDao;
    this.urlMappingDao = urlMappingDao;
    this.statisticsDao = statisticsDao;
  }

  public AccountResponse createAccount(String accountId){
    val password = generatePassword();
    try {
      accountsDao.createAccount(accountId, getHash(password));
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

  public String register(String url, Integer redirectType, String accountId) {
    String shortName = urlMappingDao.getNextAvailableName();
    urlMappingDao.createMapping(url, shortName, redirectType, accountId);
    return shortName;
  }

  public Map<String, Integer> getStatistics(String accountId) {
    return statisticsDao.getStatsByAccountId(accountId);
  }

  public boolean authenticateAccount(Account account) {
    return accountsDao.authenticateAccount(account.getAccountId(), getHash(account.getPassword()));
  }


  private String generateShortName() {
    return RandomStringUtils.randomAlphanumeric(shortUrlLength.get());
  }

  private String getHash(String password) {
    return new String(hasher.digest(password.getBytes()));
  }

  private String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(passwordLength);
  }



}
