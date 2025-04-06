package com.portfolio.app.core.service;

import com.portfolio.app.core.producer.StockPriceChangeEventProducerRunnable;
import com.portfolio.data.StockMarket;
import com.portfolio.dto.event.StockChangePriceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;

/**
 * The type Market data service is a daemon thread running in background to simulate Stock Market
 */
@Component
public class MarketDataService extends Thread{

  private final StockPriceChangeEventProducerRunnable stockPriceChangeEventProducerRunnable;

  /**
   * Instantiates a new Market data service.
   *
   * @param stockChangePriceEventBlockingQueue the stock change price event blocking queue
   * @param stockMarket                        the stock market
   * @param stockPriceSimulateDurationMin      the stock price simulate duration min
   * @param stockPriceSimulateDurationMax      the stock price simulate duration max
   */
  @Autowired
  public MarketDataService(
    BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue,
    StockMarket stockMarket,
    @Value("${stock.price.simulate.duration.min:500}") Integer stockPriceSimulateDurationMin,
    @Value("${stock.price.simulate.duration.max:2000}") Integer stockPriceSimulateDurationMax
  ){
    this.stockPriceChangeEventProducerRunnable = new StockPriceChangeEventProducerRunnable(
            stockChangePriceEventBlockingQueue, stockMarket,
            stockPriceSimulateDurationMin, stockPriceSimulateDurationMax);
  }

  /**
   * Start simulate.
   */
  @PostConstruct
  public void startSimulate(){
    Thread simulatorThread = new Thread(stockPriceChangeEventProducerRunnable);
    simulatorThread.setDaemon(true);
    simulatorThread.start();
  }

  /**
   * Stop simulate.
   */
  @PreDestroy
  public void stopSimulate(){
    stockPriceChangeEventProducerRunnable.stop();
  }
}
