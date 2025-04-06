package com.portfolio.app.core.service.Impl;

import com.portfolio.app.core.console.PortfolioConsole;
import com.portfolio.app.core.consumer.StockPriceUpdateConsumer;
import com.portfolio.app.core.service.IBrokerService;
import com.portfolio.data.StockPriceCache;
import com.portfolio.data.UserProfile;
import com.portfolio.dto.event.StockPriceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BrokerService implements IBrokerService {

  private final BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue;
  private final StockPriceCache stockPriceCache;
  private final PortfolioConsole portfolioConsole;
  private final Object displayBlockingLock;

  private final List<UserProfile> subscribers;
  private final List<StockPriceUpdateConsumer> stockPriceUpdateConsumers;
  private static final int NUM_CONSUMERS = 4;

  private static final Logger logger = LoggerFactory.getLogger(BrokerService.class);

  @Autowired
  public BrokerService(
    BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue,
    StockPriceCache stockPriceCache,
    PortfolioConsole portfolioConsole,
    Object displayBlockingLock
  ){
    this.stockPriceEventBlockingQueue = stockPriceEventBlockingQueue;
    this.stockPriceCache = stockPriceCache;
    this.portfolioConsole = portfolioConsole;
    this.displayBlockingLock = displayBlockingLock;
    this.subscribers = new ArrayList<>();
    this.stockPriceUpdateConsumers = new ArrayList<>();
  }

  @Override
  public void registerUserProfile(UserProfile userProfile){
    logger.debug("Register New UserProfile: {}", userProfile.getPortfolio().getName());
    subscribers.add(userProfile);
    portfolioConsole.registerPortfolio(userProfile.getPortfolio());
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
