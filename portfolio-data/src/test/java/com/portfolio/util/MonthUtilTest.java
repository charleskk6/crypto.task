package com.portfolio.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthUtilTest {

  private MonthUtil target;

  @Test
  void test_JAN(){
    Integer actual = MonthUtil.MONTH_MAP.get("JAN");
    assertEquals(1, actual);
  }

  @Test
  void test_Invalid_Month(){
    Integer actual = MonthUtil.MONTH_MAP.get("MON");
    assertEquals(null, actual);
  }
}