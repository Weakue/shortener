package com.infobip.shortener.dao;

import lombok.val;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class HashMapStatisticsDao implements StatisticsDao {

  private Map<String, Map<String, Integer>> repository = new HashMap<>();

  @Override
  public Map<String, Integer> getStatsByAccountId(String accountId) {
    val stats = repository.get(accountId);

    return stats == null
        ? null
        : Collections.unmodifiableMap(stats);
  }

  @Override
  public void increaseCounter(String accountId, String url) {
    repository.compute(accountId, (id, stats) -> {
      stats.compute(url, (existedUrl, counter) -> increaseCounter(counter));
      return stats;
    });
  }

  private static Integer increaseCounter(Integer counter) {
    if (counter == null) {
      counter = 1;
    } else {
      counter++;
    }
    return counter;
  }
}
