package com.infobip.shortener.service;

import com.infobip.shortener.dao.StatisticsDao;
import com.infobip.shortener.dao.UrlMappingDao;
import com.infobip.shortener.service.model.RedirectEntry;

import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RedirectService {

  @Autowired
  StatisticsDao statisticsDao;

  @Autowired
  UrlMappingDao urlMappingDao;

  public RedirectEntry processRedirect(String shortName) {
    val redirectEntry = urlMappingDao.getUrlByShortName(shortName);

    val id = urlMappingDao
        .getAccountIdForShortName(shortName);
    val url = urlMappingDao.getUrlByShortName(shortName)
        .getUrl();
    statisticsDao.increaseCounter(id, url);
    return redirectEntry;
  }

}
