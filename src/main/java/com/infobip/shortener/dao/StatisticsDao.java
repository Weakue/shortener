package com.infobip.shortener.dao;

import java.util.Map;

public interface StatisticsDao {

  /**
   * Returns stats map by accId.
   * By performance reasons returned data may not be up to date.
   */
  Map<String, Integer> getStatsByAccountId(String accountId);

  /**
   * Incapsulates logic to increment a counter for stats.
   */
  void increaseCounter(String accountId, String shortName);

  /**
   * Incapsulates logic to set a counter for stats.
   */
  void setCounter(String accountId, String url, Integer value);
}
