package com.infobip.shortener.dao;

public interface UrlMappingDao {

  void createMapping(String url, String shortName, Integer redirectType) throws IllegalStateException;

  String getUrlByShortName(String shortName);
}
