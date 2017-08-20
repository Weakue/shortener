package com.infobip.shortener.dao;

import com.infobip.shortener.service.model.RedirectEntry;

public interface UrlMappingDao {

  /**
   * Saves a mapping in a system.
   */
  void createMapping(String url, String shortName, Integer redirectType, String account);

  RedirectEntry getUrlByShortName(String shortName);

  String getAccountIdForShortName(String shortName);

  /**
   * Generates unique(!) name which is not exists in the system.
   * @return next available name.
   */
  String getNextAvailableName();
}
