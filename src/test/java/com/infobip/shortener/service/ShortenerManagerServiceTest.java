package com.infobip.shortener.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infobip.shortener.dao.AccountsDao;
import com.infobip.shortener.dao.StatisticsDao;
import com.infobip.shortener.dao.UrlMappingDao;
import com.infobip.shortener.exception.AccountAlreadyExistedException;
import com.infobip.shortener.service.model.Account;

import lombok.SneakyThrows;
import lombok.val;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.MessageDigest;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class ShortenerManagerServiceTest {


  @InjectMocks
  private
  ShortenerManagerService service;

  @Mock
  private AccountsDao accountsDao;
  @Mock
  private UrlMappingDao urlMappingDao;
  @Mock
  private StatisticsDao statisticsDao;


  @Before
  public void setup() {
    // to avoid using spring runner
    ReflectionTestUtils.setField(service, "passwordLength", 8);
    ReflectionTestUtils.setField(service, "hasher", getHasher());
    ReflectionTestUtils.setField(service, "baseServiceUrl", "localhost");
  }

  @SneakyThrows
  private MessageDigest getHasher() {
    return MessageDigest.getInstance("SHA-256");
  }

  @Test
  @SneakyThrows
  public void validAccountCreation() {
    val result = service.createAccount("someId");
    verify(accountsDao, times(1))
        .createAccount(anyString(), anyString());

    assertTrue(result.isSuccess());
    assertTrue("Password should be generated", result.getPassword() != null);
    assertTrue("Password should be generated", !result.getPassword().isEmpty());
  }

  @Test
  @SneakyThrows
  public void existedAccountCreation() {
    doThrow(new AccountAlreadyExistedException()).when(accountsDao).createAccount(anyString(), anyString());
    val result = service.createAccount("someId");
    verify(accountsDao, times(1))
        .createAccount(anyString(), anyString());
    assertFalse(result.isSuccess());
    assertTrue("Password should not be generated", result.getPassword() == null);
  }

  @Test
  @SneakyThrows
  public void registerShouldReturnGeneratedUrl() {
    when(urlMappingDao.getNextAvailableName()).thenReturn("1");

    val result = service.register("url", 302, "someID");
    verify(urlMappingDao, times(1))
        .createMapping(anyString(), anyString(), anyInt(), anyString());

    assertEquals("register should return generated url", "localhost/1", result);
  }

  @Test
  @SneakyThrows
  public void statisticsShouldReturnStatistics() {
    val stats = new HashMap<String, Integer>();
    stats.put("1", 1);
    when(statisticsDao.getStatsByAccountId(anyString())).thenReturn(stats);

    val result = service.getStatistics("someID");
    verify(statisticsDao, times(1))
        .getStatsByAccountId(anyString());

    assertTrue("stats shoudl be trasparently passed", stats == result);
  }

  @Test
  @SneakyThrows
  public void noAuthForNonExistedUser() {
    val result = service.authenticateAccount(null);
    verify(accountsDao, times(0))
        .isAccountWithThisHashExists(anyString(), anyString());
    assertFalse(result);
  }

  @Test
  @SneakyThrows
  public void appruvedAuthIsPassedByService() {
    when(accountsDao.isAccountWithThisHashExists(anyString(), anyString()))
        .thenReturn(true);
    val result = service.authenticateAccount(new Account("1", "1"));
    verify(accountsDao, times(1))
        .isAccountWithThisHashExists(anyString(), anyString());
    assertTrue(result);
  }

  @Test
  @SneakyThrows
  public void discardedAuthIsPassedByService() {
    when(accountsDao.isAccountWithThisHashExists(anyString(), anyString()))
        .thenReturn(false);
    val result = service.authenticateAccount(new Account("1", "1"));
    verify(accountsDao, times(1))
        .isAccountWithThisHashExists(anyString(), anyString());
    assertFalse(result);
  }

  @Test
  @SneakyThrows
  public void passwordIsHashedOnRegister() {
    ArgumentCaptor<String> hashCaptor = ArgumentCaptor.forClass(String.class);
    doNothing().when(accountsDao).createAccount(anyString(), hashCaptor.capture());

    val result = service.createAccount("someId");

    verify(accountsDao, times(1))
        .createAccount(anyString(), anyString());

    assertEquals(new String(getHasher().digest(result.getPassword().getBytes("UTF-8"))),
        hashCaptor.getValue());

  }

}