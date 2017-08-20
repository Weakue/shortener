package com.infobip.shortener.rest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infobip.shortener.service.RedirectService;
import com.infobip.shortener.service.model.RedirectEntry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(RedirectController.class)
public class RedirectControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  RedirectService service;

  @Test
  public void foundResponse() throws Exception {
    when(service.processRedirect(any()))
        .thenReturn(new RedirectEntry("url", 302));

    mockMvc.perform(get("/someShortUrl")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isFound())
        .andExpect(header().string("Location", "url"));
  }

  @Test
  public void movedPermanentlyResponse() throws Exception {
    when(service.processRedirect(any()))
        .thenReturn(new RedirectEntry("url", 301));

    mockMvc.perform(get("/someShortUrl")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isMovedPermanently())
        .andExpect(header().string("Location", "url"));
  }

  @Test
  public void notExistedLink() throws Exception {
    when(service.processRedirect(any()))
        .thenReturn(null);

    mockMvc.perform(get("/someShortUrl")
        .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

}
