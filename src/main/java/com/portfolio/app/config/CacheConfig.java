package com.portfolio.app.config;

import com.portfolio.data.StockMarket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  @Bean
  public StockMarket setupStockPriceCache(StocksConfig stockConfig){
    // A data storage simulating Market Data
    return new StockMarket(stockConfig.getData());
  }
}
