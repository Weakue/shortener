package com.infobip.shortener.dao;

import com.infobip.shortener.service.model.RedirectEntry;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapUrlMappingDao implements UrlMappingDao {

  private Map<String, RedirectEntry> redirectEntryRepository = new ConcurrentHashMap<>();

  private Map<String, String> shortNameToAccountRepository = new ConcurrentHashMap<>();

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
}
