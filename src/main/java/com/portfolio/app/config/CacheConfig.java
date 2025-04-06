package com.portfolio.app.config;

import com.portfolio.data.StockPriceCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  @Bean
  public StockPriceCache setupStockPriceCache(StocksConfig stockConfig){
    // A data storage simulating Market Data
    return new StockPriceCache(stockConfig.getData());
  }
}
