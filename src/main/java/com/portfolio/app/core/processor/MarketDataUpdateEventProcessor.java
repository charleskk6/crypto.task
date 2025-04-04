package com.portfolio.app.core.processor;

import com.portfolio.app.core.consumer.StockPriceUpdateConsumer;
import com.portfolio.data.PortfolioCache;
import com.portfolio.data.StockPriceCache;
import com.portfolio.dto.StockPriceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MarketDataUpdateEventProcessor {

  private final BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue;
  private final StockPriceCache stockPriceCache;

  private final List<StockPriceUpdateConsumer> consumers;
  private static final int NUM_CONSUMERS = 4;

  @Autowired
  public MarketDataUpdateEventProcessor(
    BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue,
    StockPriceCache stockPriceCache
  ){
    this.stockPriceEventBlockingQueue = stockPriceEventBlockingQueue;
    this.stockPriceCache = stockPriceCache;

    this.consumers = new ArrayList<>();
  }

  @PostConstruct
  public void startConsumers(){
    ExecutorService consumerPool = Executors.newFixedThreadPool(NUM_CONSUMERS);

    for (int i = 1; i <= NUM_CONSUMERS; i++) {
      StockPriceUpdateConsumer consumer = new StockPriceUpdateConsumer(
              stockPriceEventBlockingQueue, stockPriceCache, "Consumer-" + i);
      consumers.add(consumer);
      consumerPool.submit(consumer);
    }
  }

  @PreDestroy
  public void stopConsumers(){
    for(StockPriceUpdateConsumer consumer: consumers){
      consumer.stop();
    }
  }
}
