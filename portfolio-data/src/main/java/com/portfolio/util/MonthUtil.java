package com.portfolio.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonthUtil {
  public static final Map<String, Integer> MONTH_MAP = createMonthMap();

  private static Map<String, Integer> createMonthMap() {
    Map<String, Integer> map = new HashMap<>();
    map.put("JAN", 1);
    map.put("FEB", 2);
    map.put("MAR", 3);
    map.put("APR", 4);
    map.put("MAY", 5);
    map.put("JUN", 6);
    map.put("JUL", 7);
    map.put("AUG", 8);
    map.put("SEP", 9);
    map.put("OCT", 10);
    map.put("NOV", 11);
    map.put("DEC", 12);

    return Collections.unmodifiableMap(map); // Make it immutable
  }
}
