package com.infobip.shortener.dao;

public interface UrlMappingDao {

  void createMapping(String url, String shortName, Integer redirectType);

  String getUrlByShortName(String shortName);
}
