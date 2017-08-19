package com.infobip.shortener.service;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountResponse {

  boolean success;

  String description;

  String password;
}
