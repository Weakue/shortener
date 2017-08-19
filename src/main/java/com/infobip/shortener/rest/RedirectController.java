package com.infobip.shortener.rest;

import com.infobip.shortener.service.RedirectService;

import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

  @Autowired
  RedirectService redirectService;

  @RequestMapping(method = RequestMethod.GET, path = "/{shortUrl}")
  public ResponseEntity<String> redirect(@PathVariable("shortUrl") String shortUrl) {
    val redirectEntry = redirectService.processRedirect(shortUrl);

    if (redirectEntry != null) {
      val headers = new HttpHeaders();
      headers.add("Location", redirectEntry.getUrl());
      return new ResponseEntity<>(headers, HttpStatus.valueOf(redirectEntry.getResponseCode()));
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

  }

}
