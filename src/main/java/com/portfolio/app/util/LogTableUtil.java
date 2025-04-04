package com.portfolio.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTableUtil {

  private static final Logger logger = LoggerFactory.getLogger(LogTableUtil.class);

  public static void logTable(String[] headers, String[][] rows, int[] widths) {
    StringBuilder sb = new StringBuilder();

    sb.append(formatRow(headers, widths));
    sb.append(formatSeparator(widths));

    for (String[] row : rows) {
      sb.append(formatRow(row, widths));
    }

    logger.info("\n" + sb.toString());
  }

  private static String formatRow(String[] row, int[] widths) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < row.length; i++) {
      sb.append(String.format("%-" + widths[i] + "s", row[i]));
      sb.append(" | ");
    }
    sb.setLength(sb.length() - 3); // Remove trailing " | "
    sb.append("\n");
    return sb.toString();
  }

  private static String formatSeparator(int[] widths) {
    StringBuilder sb = new StringBuilder();
    for (int width : widths) {
      sb.append(repeat("-", width)).append("-+-");
    }
    sb.setLength(sb.length() - 3); // Remove trailing "-+-"
    sb.append("\n");
    return sb.toString();
  }

  private static String repeat(String str, int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(str);
    }
    return sb.toString();
  }
}
