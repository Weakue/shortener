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

    //TODO(apuks): There is a classic lost update case, think/send questions
    Map<String, Integer> stats = repository.get(accountId);
    if (stats == null) {
      stats = new HashMap<>();
      stats.put(url, 1);
      repository.put(accountId, stats);
    } else {
      stats.compute(url, (existedUrl,counter) -> counter++);
    }

  }

}
