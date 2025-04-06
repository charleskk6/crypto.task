package com.portfolio.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfig {

  /**
   * Define a reentrant lock for blocking PortfolioConsole in waiting, until Stock Prices are all updated
   * @return Object as a lock
   */
  @Bean
  public Object displayBlockingLock(){
    return new Object();
  }
}
