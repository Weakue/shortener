package com.infobip.shortener.dao;

import com.infobip.shortener.service.model.RedirectEntry;

import lombok.val;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MapUrlMappingDao implements UrlMappingDao {
  private static final int CAPITAL_POOL = 26;
  private static final int NON_CAPITAL_POOL = 26;
  private static final int NUMBER_POOL = 10;
  private static final int POOL = CAPITAL_POOL + NON_CAPITAL_POOL + NUMBER_POOL;


  private Map<String, RedirectEntry> redirectEntryRepository = new ConcurrentHashMap<>();
  private Map<String, String> shortNameToAccountRepository = new ConcurrentHashMap<>();
  private AtomicInteger sequenceCounter = new AtomicInteger();

  private static String intToCharSequence(int n) {
    val stringBuilder = new StringBuilder();
    while (n > 0) {
      --n;
      final int remainder = n % POOL;
      if (remainder < CAPITAL_POOL) {
        stringBuilder.append((char) ('A' + remainder));
      } else if (remainder < CAPITAL_POOL + NON_CAPITAL_POOL) {
        stringBuilder.append((char) ('a' + remainder - CAPITAL_POOL));
      } else {
        stringBuilder.append((remainder - (CAPITAL_POOL + NON_CAPITAL_POOL)));
      }
      n /= POOL;
    }
    return stringBuilder.reverse()
        .toString();
  }

  @Override
  public void createMapping(String url, String shortName, Integer redirectType,
                            String accountId) {
    redirectEntryRepository.put(shortName, new RedirectEntry(url, redirectType));
    shortNameToAccountRepository.put(shortName, accountId);
  }

  @Override
  public RedirectEntry getUrlByShortName(String shortName) {
    return redirectEntryRepository.get(shortName);
  }

  @Override
  public String getAccountIdForShortName(String shortName) {
    return shortNameToAccountRepository.get(shortName);
  }

  /*It looks like business logic in a dao layer.
  Therefore there was no special requirement to use some generational strategy,
  so seed can be generated from RDBMS sequence for e.g.*/
  @Override
  public synchronized String getNextAvailableName() {
    return intToCharSequence(sequenceCounter.getAndIncrement());
  }
}
