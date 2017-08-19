package com.infobip.shortener.dao;

public interface AccountsDao {

  void createAccount(String id, String password) throws IllegalStateException;

}
