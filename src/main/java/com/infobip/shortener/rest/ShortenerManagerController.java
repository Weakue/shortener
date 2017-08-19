package com.infobip.shortener.rest;


import com.infobip.shortener.rest.dto.AccountRequestDto;
import com.infobip.shortener.rest.dto.AccountResponseDto;
import com.infobip.shortener.rest.dto.ShortUrlRequestDto;
import com.infobip.shortener.rest.dto.ShortenedUrlResponseDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class ShortenerManagerController {

  @RequestMapping(path = "/account", method = RequestMethod.POST)
  public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto requestDto) {
    throw new UnsupportedOperationException("not done yet");
  }

  @RequestMapping(path = "/register", method = RequestMethod.POST)
  public ResponseEntity<ShortenedUrlResponseDto> register(@RequestBody ShortUrlRequestDto requestDto) {
    throw new UnsupportedOperationException("not done yet");
  }

  @RequestMapping(path = "/statistic/{AccountId}", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Integer>> getStatistic(@PathVariable("AccountId") String accountId) {
    throw new UnsupportedOperationException("not done yet");
  }

}
