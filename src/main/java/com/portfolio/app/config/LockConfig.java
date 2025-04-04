package com.portfolio.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfig {

  @Bean
  public Object displayBlockingLock(){
    return new Object();
  }
}
