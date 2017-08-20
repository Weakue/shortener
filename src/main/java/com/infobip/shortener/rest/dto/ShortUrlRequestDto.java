package com.infobip.shortener.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ShortUrlRequestDto {

  String url;
  Integer redirectType = 302;

}
