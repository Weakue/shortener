package com.infobip.shortener.dao;

import com.infobip.shortener.exception.AccountAlreadyExistedException;

import lombok.val;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapAccountDao implements AccountsDao {

  private ConcurrentHashMap<String, String> repository = new ConcurrentHashMap<>();

  @Override
  public void createAccount(String id, String hash) throws AccountAlreadyExistedException {
    synchronized (this) {
      if (repository.contains(id)) {
        throw new AccountAlreadyExistedException();
      } else {
        repository.put(id, hash);
      }
    }
  }

  @Override
  public boolean isAccountWithThisHashExists(String id, String hash) {
    val storedHash = repository.get(id);

    return hash != null
        && storedHash != null
        && hash.equals(storedHash);
  }
}
