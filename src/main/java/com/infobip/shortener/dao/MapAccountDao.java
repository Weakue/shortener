package com.infobip.shortener.dao;

import com.infobip.shortener.exception.AccountAlreadyExistedException;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapAccountDao implements AccountsDao{

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
  public boolean authenticateAccount(String id, String hash) {
    return repository.getOrDefault(id, null)
        .equals(hash);
  }
}
