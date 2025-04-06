package com.portfolio.app.config;

import com.portfolio.dto.dict.Stock;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "stocks")
public class StocksConfig {
  private Map<String, Stock> data;

  public Map<String, Stock> getData() {
    return data;
  }

  public void setData(Map<String, Stock> data) {
    this.data = data;
  }
}