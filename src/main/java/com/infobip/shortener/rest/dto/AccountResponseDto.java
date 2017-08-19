package com.infobip.shortener.rest.dto;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountResponseDto {

  String success;

  String description;

  String password;

}
