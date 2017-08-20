package com.infobip.shortener.rest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infobip.shortener.rest.dto.AccountRequestDto;
import com.infobip.shortener.rest.dto.ShortUrlRequestDto;
import com.infobip.shortener.service.ShortenerManagerService;
import com.infobip.shortener.service.model.AccountResponse;

import lombok.SneakyThrows;
import lombok.val;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortenerManagerController.class)
public class ShortenerManagerControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  ShortenerManagerService service;

  @Test
  public void goodAccount() throws Exception {
    when(service.createAccount(any()))
        .thenReturn(AccountResponse.builder()
            .success(true)
            .description("Created")
            .password("12345")
            .build());

    mockMvc.perform(post("/account")
        .content(prepareAccountDto("myAccountId"))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  public void badAccount() throws Exception {
    when(service.createAccount(any()))
        .thenReturn(AccountResponse.builder()
            .success(false)
            .description("Failed")
            .build());

    mockMvc.perform(post("/account")
        .content(prepareAccountDto("myAccountId"))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void noAuthHeaderRegister() throws Exception {
    mockMvc.perform(post("/register")
        .content(preprateShortUrlRequest("localhost:8080"))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void noAccountRegister() throws Exception {
    when(service.authenticateAccount(any()))
        .thenReturn(false);

    mockMvc.perform(post("/register")
        .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
        .content(preprateShortUrlRequest("anyUrl"))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void registeredAccount() throws Exception {
    when(service.authenticateAccount(any()))
        .thenReturn(true);

    mockMvc.perform(post("/register")
        .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
        .content(preprateShortUrlRequest("http://stackoverflow.com/questions/1567929/website-safe-data-"
            + "access-architecture-question?rq=1"))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  public void malformedUrl() throws Exception {
    when(service.authenticateAccount(any()))
        .thenReturn(true);

    mockMvc.perform(post("/register")
        .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
        .content(preprateShortUrlRequest("htp://stackoverflow.com/questions/1567929/website-safe-data-"
            + "access-architecture-question?rq=1"))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void noHeaderStats() throws Exception {
    mockMvc.perform(get("/statistic/someAcc")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void noAccountStats() throws Exception {
    when(service.authenticateAccount(any()))
        .thenReturn(false);

    mockMvc.perform(get("/statistic/someAcc")
        .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
        .contentType(APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void existedAccountStats() throws Exception {
    when(service.authenticateAccount(any()))
        .thenReturn(true);
    val stats = new HashMap<String, Integer>();
    stats.put("testUrl", 1);
    when(service.getStatistics(any()))
        .thenReturn(stats);

    mockMvc.perform(get("/statistic/someAcc")
        .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void nullStats() throws Exception {
    when(service.authenticateAccount(any()))
        .thenReturn(true);
    when(service.getStatistics(any()))
        .thenReturn(Collections.emptyMap());

    mockMvc.perform(get("/statistic/someAcc")
        .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void emptyStats() throws Exception {
    when(service.authenticateAccount(any()))
        .thenReturn(true);
    when(service.getStatistics(any()))
        .thenReturn(null);

    mockMvc.perform(get("/statistic/someAcc")
        .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }


  @SneakyThrows
  private String getAuthHeader() {
    return "Basic " + Base64
        .getEncoder()
        .encodeToString("login:pwd".getBytes("UTF-8"));
  }


  @SneakyThrows
  private String prepareAccountDto(String name) {
    val dto = new AccountRequestDto(name);
    return objectMapper.writeValueAsString(dto);
  }

  @SneakyThrows
  private String preprateShortUrlRequest(String url) {
    val dto = new ShortUrlRequestDto();
    dto.setUrl(url);
    return objectMapper.writeValueAsString(dto);
  }

}
