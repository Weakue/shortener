package com.infobip.shortener.dao;


import static org.junit.Assert.fail;

import lombok.val;

import org.junit.Test;

import java.util.HashSet;

public class MapUrlMappingDaoTest {

  @Test
  public void test() {
    val dao = new MapUrlMappingDao();
    HashSet<String> set = new HashSet<>();
    //On my mbp this runs 0.581 secs, so i think thats appropriate. No guaranties still
    for(int i = 0; i < 1000000; i++) {
      if (!set.add(dao.getNextAvailableName())) {
        fail();
      }
    }

  }

}