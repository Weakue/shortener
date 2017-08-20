package com.infobip.shortener.dao;

import static java.util.stream.Collectors.toMap;

import lombok.val;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HashMapStatisticsDao implements StatisticsDao {

  private Map<String, Map<String, Integer>> repository = new ConcurrentHashMap<>();

  @Override
  public Map<String, Integer> getStatsByAccountId(String accountId) {
    val stats = repository.get(accountId);

    return stats == null
        ? null
        : stats.entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)); // collect a copy to unmodifiable map.
  }

  @Override
  public void increaseCounter(String accountId, String url) {
    CompletableFuture.runAsync(() -> increaseCounterSync(accountId, url));
  }

  @Override
  public void setCounter(String accountId, String url, Integer value) {
    CompletableFuture.runAsync(() -> setCounterSync(accountId, url, value));
  }

  //Not sure that there is so much synchronized block needed
  private synchronized void setCounterSync(String accountId, String url, Integer value) {
    Map<String, Integer> stats = repository.get(accountId);
    if (stats == null) {
      stats = new HashMap<>();
      stats.put(url, value);
    } else {
      stats.compute(url, (existedUrl, counter) -> value);
    }
    repository.put(accountId, stats);
  }

  private synchronized void increaseCounterSync(String accountId, String url) {
    Map<String, Integer> stats = repository.get(accountId);
    if (stats == null) {
      stats = new HashMap<>();
      stats.put(url, 1);
    } else {
      stats.compute(url, (existedUrl, counter) -> counter++);
    }
    repository.put(accountId, stats);
  }

}
