package com.portfolio.app.config;

import com.portfolio.app.core.service.impl.BrokerService;
import com.portfolio.data.Portfolio;
import com.portfolio.data.StockMarket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Portfolio config.
 */
@Configuration
public class PortfolioConfig {

  // Boostrap a Demo Portfolio
  @Bean
  public Portfolio portfolio(
          StockMarket stockMarket,
          BrokerService brokerService
          ){
    Portfolio portfolio = new Portfolio("Portfolio", "/position.csv");
    portfolio.setupPortfolio(stockMarket);
    brokerService.registerPortfolio(portfolio);
    return portfolio;
  }
}
