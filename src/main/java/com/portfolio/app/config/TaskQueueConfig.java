package com.portfolio.app.config;

import com.portfolio.dto.event.StockChangePriceEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The type Task queue config. Create Bean for a stock Price change event BlockingQueue
 */
@Configuration
public class TaskQueueConfig {

  /**
   * Stock price event blocking queue
   *
   * @return the blocking queue
   */
  @Bean
  public BlockingQueue<StockChangePriceEvent> stockPriceEventBlockingQueue(){
     return new LinkedBlockingQueue<>();
  }
}
