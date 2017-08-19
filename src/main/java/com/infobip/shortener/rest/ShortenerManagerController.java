package com.infobip.shortener.rest;


import static com.infobip.shortener.rest.dto.AccountResponseDto.toDto;

import com.infobip.shortener.rest.dto.AccountRequestDto;
import com.infobip.shortener.rest.dto.AccountResponseDto;
import com.infobip.shortener.rest.dto.ShortUrlRequestDto;
import com.infobip.shortener.rest.dto.ShortenedUrlResponseDto;
import com.infobip.shortener.service.ShortenerManagerService;
import com.infobip.shortener.service.model.Account;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Map;

@Slf4j
@RestController
public class ShortenerManagerController {

  private final ShortenerManagerService managerService;

  @Autowired
  public ShortenerManagerController(ShortenerManagerService managerService) {
    this.managerService = managerService;
  }


  @RequestMapping(path = "/account", method = RequestMethod.POST)
  public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto requestDto) {
    val result = managerService.createAccount(requestDto
        .getAccountId());
    if (result.isSuccess()) {
      return new ResponseEntity<>(toDto(result), HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(toDto(result), HttpStatus.BAD_REQUEST); // is 409 CONFLICT better?
    }
  }

  @RequestMapping(path = "/register", method = RequestMethod.POST)
  public ResponseEntity<ShortenedUrlResponseDto> register(@RequestBody ShortUrlRequestDto requestDto,
                                                          @RequestHeader("Authorization") String base64Auth) {
    val account = getAccountFromAuthHeader(base64Auth);
    if (!managerService.authenticateAccount(account)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //TODO(apuks): validate url

    val result = managerService.register(requestDto.getUrl(), requestDto.getRedirectType(), account.getAccountId());
    return new ResponseEntity<>(new ShortenedUrlResponseDto(result), HttpStatus.OK);
  }


  @RequestMapping(path = "/statistic/{AccountId}", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Integer>> getStatistic(@PathVariable("AccountId") String accountId,
                                                           @RequestHeader("Authorization") String base64Auth) {
    val account = getAccountFromAuthHeader(base64Auth);
    if (!managerService.authenticateAccount(account)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    val result = managerService.getStatistics(accountId);

    if (result == null || result.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(result, HttpStatus.OK);
    }
  }

  private Account getAccountFromAuthHeader(@RequestHeader("Authorization") String base64Auth) {
    val splitted = base64Auth.split(" ");



    return Account.createAccountFromAuthHeader(new String(Base64
        .getDecoder()
        .decode(splitted[1])));
  }


}
