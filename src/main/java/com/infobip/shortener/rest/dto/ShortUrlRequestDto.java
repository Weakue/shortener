package com.infobip.shortener.rest.dto;

import lombok.Data;

@Data
public class ShortUrlRequestDto {

  String url;
  Integer redirectType = 302;

}
