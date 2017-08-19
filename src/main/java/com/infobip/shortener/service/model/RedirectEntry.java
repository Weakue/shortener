package com.infobip.shortener.service.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class RedirectEntry {

  @NonNull
  String url;

  int responseCode;

}
