package com.infobip.shortener.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class AccountRequestDto {

  @JsonProperty(value = "AccountId", required = true)
  String accountId;

}
