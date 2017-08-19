package com.infobip.shortener.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

  @RequestMapping(method = RequestMethod.GET, path = "/")
  public ResponseEntity redirect() {
    throw new UnsupportedOperationException("not done yet");
  }

}
