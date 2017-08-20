package com.infobip.shortener.dao;

import java.util.Map;

public interface StatisticsDao {

  Map<String, Integer> getStatsByAccountId(String accountId);

  void increaseCounter(String accountId, String shortName);

  void setCounter(String accountId, String url, Integer value);
}
