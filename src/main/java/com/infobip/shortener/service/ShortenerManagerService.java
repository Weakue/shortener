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

@Service
public class ShortenerManagerService {

  private final MessageDigest hasher;
  private final AccountsDao accountsDao;
  private final UrlMappingDao urlMappingDao;
  private final StatisticsDao statisticsDao;
  @Value("${app.password.length:8}")
  private Integer passwordLength;

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

  public AccountResponse createAccount(String accountId) {
    val password = generatePassword();
    //Probably can be rewritten to some optionals, but i believe that it depends on real datasource.
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
    return account != null
        && accountsDao.authenticateAccount(account.getAccountId(), getHash(account.getPassword()));
  }


  private String getHash(String password) {
    return new String(hasher.digest(password.getBytes()));
  }

  private String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(passwordLength);
  }


}
