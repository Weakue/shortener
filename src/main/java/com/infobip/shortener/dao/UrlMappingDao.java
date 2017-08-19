package com.infobip.shortener.dao;

import com.infobip.shortener.service.model.RedirectEntry;

public interface UrlMappingDao {

  void createMapping(String url, String shortName, Integer redirectType);

  RedirectEntry getUrlByShortName(String shortName);

  String getAccountIdForShortName(String shortName);
}
