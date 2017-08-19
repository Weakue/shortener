package com.infobip.shortener.dao;

import com.infobip.shortener.exception.AccountAlreadyExistedException;

public interface AccountsDao {

  void createAccount(String id, String password) throws AccountAlreadyExistedException;

}
