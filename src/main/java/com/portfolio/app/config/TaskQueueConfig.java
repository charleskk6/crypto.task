package com.portfolio.app.config;

import com.portfolio.dto.event.StockPriceEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class TaskQueueConfig {

  @Bean
  public BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue(){
     return new LinkedBlockingQueue<>();
  }

  @Bean
  public BlockingQueue<String> portfolioEventBlockingQueue(){
    return new LinkedBlockingQueue<>();
  }
}
