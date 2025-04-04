package com.portfolio.app.config;

import com.portfolio.data.PortfolioCache;
import com.portfolio.data.StockPriceCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  @Value("${stock.price.lower.bound:1}")
  private Integer stockPriceLowerBound;
  @Value("${stock.price.upper.bound:1000}")
  private Integer stockPriceUpperBound;

  @Bean
  public StockPriceCache setupStockPriceCache(){
    return new StockPriceCache(stockPriceLowerBound, stockPriceUpperBound);
  }

  @Bean
  public PortfolioCache setupPortfolioCache(StockPriceCache stockPriceCache){
    return new PortfolioCache(stockPriceCache);
  }
}
