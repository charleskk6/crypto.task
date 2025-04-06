package com.portfolio.app.config;

import com.portfolio.data.StockMarket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockMarketConfig {

  /**
   * Setup StockMarket by StocksConfig
   * @param stocksConfig constructed by application properties
   * @return stockMarket
   */
  @Bean
  public StockMarket setupStockMarket(StocksConfig stocksConfig){
    return new StockMarket(stocksConfig.getData(), null);
  }
}
