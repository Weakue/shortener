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

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
  private UrlValidator urlValidator = new UrlValidator();

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
  public ResponseEntity<ShortenedUrlResponseDto> register(
      @RequestBody ShortUrlRequestDto requestDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

    if (authHeader == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    val account = getAccountFromAuthHeader(authHeader);
    if (!managerService.authenticateAccount(account)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    if (!urlValidator.isValid(requestDto.getUrl())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    val result = managerService.register(requestDto.getUrl(), requestDto.getRedirectType(), account.getAccountId());
    return new ResponseEntity<>(new ShortenedUrlResponseDto(result), HttpStatus.CREATED);
  }


  @RequestMapping(path = "/statistic/{AccountId}", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Integer>> getStatistic(
      @PathVariable("AccountId") String accountId,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

    if (authHeader == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    val account = getAccountFromAuthHeader(authHeader);
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

  private Account getAccountFromAuthHeader(String base64Auth) {
    val splitted = base64Auth.split(" "); //Should i precompile this?

    return Account.createAccountFromAuthHeader(new String(Base64
        .getDecoder()
        .decode(splitted[1])));
  }


}
