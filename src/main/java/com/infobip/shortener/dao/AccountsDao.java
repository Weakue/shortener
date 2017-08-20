package com.infobip.shortener.dao;

import com.infobip.shortener.exception.AccountAlreadyExistedException;

public interface AccountsDao {

  void createAccount(String id, String hash) throws AccountAlreadyExistedException;

  boolean isAccountWithThisHashExists(String id, String hash);

}
