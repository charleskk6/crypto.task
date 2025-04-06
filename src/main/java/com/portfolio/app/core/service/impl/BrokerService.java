package com.portfolio.app.core.service.impl;

import com.portfolio.app.core.console.PortfolioConsole;
import com.portfolio.app.core.consumer.StockPriceUpdateConsumer;
import com.portfolio.app.core.service.IBrokerService;
import com.portfolio.data.Portfolio;
import com.portfolio.data.StockMarket;
import com.portfolio.dto.event.StockChangePriceEvent;
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

  private final BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue;
  private final StockMarket stockMarket;
  private final PortfolioConsole portfolioConsole;
  private final Object displayBlockingLock;

  private final List<Portfolio> subscribers;
  private final List<StockPriceUpdateConsumer> stockPriceUpdateConsumers;
  private static final int NUM_CONSUMERS = 4;

  private static final Logger logger = LoggerFactory.getLogger(BrokerService.class);

  @Autowired
  public BrokerService(
    BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue,
    StockMarket stockMarket,
    PortfolioConsole portfolioConsole,
    Object displayBlockingLock
  ){
    this.stockChangePriceEventBlockingQueue = stockChangePriceEventBlockingQueue;
    this.stockMarket = stockMarket;
    this.portfolioConsole = portfolioConsole;
    this.displayBlockingLock = displayBlockingLock;
    this.subscribers = new ArrayList<>();
    this.stockPriceUpdateConsumers = new ArrayList<>();
  }

  @Override
  public void registerPortfolio(Portfolio portfolio){
    logger.debug("Register New Portfolio: {}", portfolio.getName());
    subscribers.add(portfolio);
    portfolioConsole.registerPortfolio(portfolio);
  }

  @PostConstruct
  public void startConsumers(){
    ExecutorService consumerPool = Executors.newFixedThreadPool(NUM_CONSUMERS);

    for (int i = 1; i <= NUM_CONSUMERS; i++) {
      StockPriceUpdateConsumer consstockPriceUpdateConsumer = new StockPriceUpdateConsumer(
              stockChangePriceEventBlockingQueue, stockMarket, subscribers, "Consumer-" + i, displayBlockingLock);
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
