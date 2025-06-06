package com.portfolio.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Log table util. Helps print Portfolio Console
 */
public class LogTableUtil {

  private static final Logger logger = LoggerFactory.getLogger(LogTableUtil.class);

  /**
   * Log table.
   *
   * @param headers the headers
   * @param rows    the rows
   * @param widths  the widths
   */
  public static void logTable(String[] headers, String[][] rows, int[] widths) {
    StringBuilder sb = new StringBuilder();

    sb.append(formatRow(headers, widths));
    sb.append(formatSeparator(widths));

    for (String[] row : rows) {
      sb.append(formatRow(row, widths));
    }

    logger.info("\n{}", sb);
  }

  private static String formatRow(String[] row, int[] widths) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < row.length; i++) {
      String value;
      try{
        value = String.format("%," + widths[i] + ".2f", Double.parseDouble(row[i]));
      } catch (NumberFormatException ex){
        value = String.format("%-" + widths[i] + "s", row[i]);
      }
      sb.append(value);
      sb.append(" | ");
    }
    sb.setLength(sb.length() - 3); // Remove trailing " | "
    sb.append("\n");
    return sb.toString();
  }

  private static String formatSeparator(int[] widths) {
    StringBuilder sb = new StringBuilder();
    for (int width : widths) {
      sb.append(repeat(width)).append("-+-");
    }
    sb.setLength(sb.length() - 3); // Remove trailing "-+-"
    sb.append("\n");
    return sb.toString();
  }

  private static String repeat(int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append("-");
    }
    return sb.toString();
  }
}
