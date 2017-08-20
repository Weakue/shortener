package com.infobip.shortener.dao;

import com.infobip.shortener.exception.AccountAlreadyExistedException;

public interface AccountsDao {

  /**
   * @param id to save
   * @param hash to save.
   * @throws AccountAlreadyExistedException when acc alrady exists.
   */
  void createAccount(String id, String hash) throws AccountAlreadyExistedException;

  /**
   * Validates if acc exists.
   */
  boolean isAccountWithThisHashExists(String id, String hash);

}
