package com.portfolio.app.core.service;

import com.portfolio.app.config.StocksConfig;
import com.portfolio.app.core.producer.StockPriceChangeEventProducer;
import com.portfolio.data.StockMarket;
import com.portfolio.dto.event.StockChangePriceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;

@Component
public class MarketDataService extends Thread{

  private final StockPriceChangeEventProducer stockPriceChangeEventProducer;

  @Autowired
  public MarketDataService(
    BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue,
    StocksConfig stockConfig,
    StockMarket stockMarket,
    @Value("${stock.price.simulate.duration.min:500}") Integer stockPriceSimulateDurationMin,
    @Value("${stock.price.simulate.duration.max:2000}") Integer stockPriceSimulateDurationMax
  ){
    this.stockPriceChangeEventProducer = new StockPriceChangeEventProducer(
            stockChangePriceEventBlockingQueue, stockMarket,
            stockPriceSimulateDurationMin, stockPriceSimulateDurationMax);
  }

  @PostConstruct
  public void startSimulate(){
    Thread simulatorThread = new Thread(stockPriceChangeEventProducer);
    simulatorThread.setDaemon(true);
    simulatorThread.start();
  }

  @PreDestroy
  public void stopSimulate(){
    stockPriceChangeEventProducer.stop();
  }
}
