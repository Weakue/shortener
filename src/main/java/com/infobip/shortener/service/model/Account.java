package com.infobip.shortener.service.model;

import lombok.Value;
import lombok.val;

@Value
public class Account {

  private String accountId;

  private String password;

  public static Account createAccountFromAuthHeader(String header) {
    val splitted = header.split(":");

    return new Account(splitted[0], splitted[1]);
  }
}
