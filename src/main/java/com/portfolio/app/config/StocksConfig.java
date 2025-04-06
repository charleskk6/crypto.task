package com.portfolio.app.config;

import com.portfolio.dto.dict.Stock;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * The type Stocks config construct initial Mock Market Data from application properties
 */
@Configuration
@ConfigurationProperties(prefix = "stocks")
public class StocksConfig {
  private Map<String, Stock> data;

  /**
   * Gets data.
   *
   * @return the data
   */
  public Map<String, Stock> getData() {
    return data;
  }

  /**
   * Sets data.
   *
   * @param data the data
   */
  public void setData(Map<String, Stock> data) {
    this.data = data;
  }
}