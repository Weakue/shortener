package com.infobip.shortener.service;

import com.infobip.shortener.dao.StatisticsDao;
import com.infobip.shortener.dao.UrlMappingDao;
import com.infobip.shortener.service.model.RedirectEntry;

import lombok.val;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedirectService {

  private final StatisticsDao statisticsDao;

  private final UrlMappingDao urlMappingDao;

  @Autowired
  public RedirectService(StatisticsDao statisticsDao, UrlMappingDao urlMappingDao) {
    this.statisticsDao = statisticsDao;
    this.urlMappingDao = urlMappingDao;
  }

  /**
   * Gets entry for given shortname. Also increases counter for this entry.
   *
   * @param shortName to process.
   * @return RedirectEntry with url and code.
   */
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
