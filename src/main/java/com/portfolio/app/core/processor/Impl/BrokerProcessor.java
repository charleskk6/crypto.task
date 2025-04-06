package com.portfolio.app.core.processor.Impl;

import com.portfolio.app.core.consumer.StockPriceUpdateConsumer;
import com.portfolio.app.core.processor.IBrokerProcessor;
import com.portfolio.data.StockPriceCache;
import com.portfolio.data.UserProfile;
import com.portfolio.dto.event.StockPriceEvent;
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
public class BrokerProcessor implements IBrokerProcessor {

  private final BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue;
  private final StockPriceCache stockPriceCache;
  private final Object displayBlockingLock;

  private final List<UserProfile> subscribers;
  private final List<StockPriceUpdateConsumer> stockPriceUpdateConsumers;
  private static final int NUM_CONSUMERS = 4;

  @Autowired
  public BrokerProcessor(
    BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue,
    StockPriceCache stockPriceCache,
    Object displayBlockingLock
  ){
    this.stockPriceEventBlockingQueue = stockPriceEventBlockingQueue;
    this.stockPriceCache = stockPriceCache;
    this.displayBlockingLock = displayBlockingLock;
    this.subscribers = new ArrayList<>();
    this.stockPriceUpdateConsumers = new ArrayList<>();
  }

  @Override
  public void registerUserProfile(UserProfile userProfile){
    subscribers.add(userProfile);
  }

  @PostConstruct
  public void startConsumers(){
    ExecutorService consumerPool = Executors.newFixedThreadPool(NUM_CONSUMERS);

    for (int i = 1; i <= NUM_CONSUMERS; i++) {
      StockPriceUpdateConsumer consstockPriceUpdateConsumer = new StockPriceUpdateConsumer(
        stockPriceEventBlockingQueue, stockPriceCache, subscribers, "Consumer-" + i, displayBlockingLock);
      stockPriceUpdateConsumers.add(consstockPriceUpdateConsumer);
      consumerPool.submit(consstockPriceUpdateConsumer);
    }
  }

  @PreDestroy
  public void stopConsumers(){
    for(StockPriceUpdateConsumer consumer: stockPriceUpdateConsumers){
      consumer.stop();
    }
  }
}
