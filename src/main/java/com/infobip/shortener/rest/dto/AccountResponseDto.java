package com.infobip.shortener.rest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.infobip.shortener.service.AccountResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponseDto {

  String success;

  String description;

  String password;

  public static AccountResponseDto toDto(AccountResponse response) {
    return AccountResponseDto.builder()
        .description(response.getDescription())
        .success(response.isSuccess()
            ? "true"
            : "false")
        .password(response.getPassword())
        .build();
  }

}
