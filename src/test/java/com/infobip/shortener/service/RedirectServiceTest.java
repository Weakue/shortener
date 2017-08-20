package com.infobip.shortener.service;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infobip.shortener.dao.StatisticsDao;
import com.infobip.shortener.dao.UrlMappingDao;
import com.infobip.shortener.service.model.RedirectEntry;

import lombok.val;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class RedirectServiceTest {

  @InjectMocks
  private RedirectService service;
  @Mock
  private UrlMappingDao urlMappingDao;
  @Mock
  private StatisticsDao statisticsDao;

  @Test
  public void redirectedEntryReturnedFromUrlMappingDao() {
    val responseFromDao = new RedirectEntry("1", 302);
    when(urlMappingDao.getUrlByShortName(anyString()))
        .thenReturn(responseFromDao);

    val result = service.processRedirect("Not1");

    assertEquals(responseFromDao, result);
  }


  @Test
  public void statsAreIncrementedOnRedirect() {
    val responseFromDao = new RedirectEntry("1", 302);
    when(urlMappingDao.getUrlByShortName(anyString()))
        .thenReturn(responseFromDao);

    service.processRedirect("Not1");

    verify(statisticsDao, times(1))
        .increaseCounter(anyString(), anyString());
  }


}