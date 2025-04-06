package com.portfolio.app.core.service;

import com.portfolio.app.core.console.PortfolioConsole;
import com.portfolio.app.core.consumer.StockPriceUpdateConsumerRunnable;
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

/**
 * The type Broker service.
 */
@Component
public class BrokerService {

  // Listen to Stock ChangePrice Event
  private final BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue;
  // Stock Market to be updated
  private final StockMarket stockMarket;
  // Portfolio Console for printing
  private final PortfolioConsole portfolioConsole;
  // Lock for waiting stock prices update complete
  private final Object displayBlockingLock;
  // Portfolio to be updated
  private final List<Portfolio> subscribers;
  // Consumes run parallel to update price change events
  private final List<StockPriceUpdateConsumerRunnable> stockPriceUpdateConsumerRunnables;
  private static final int NUM_CONSUMERS = 4;

  private static final Logger logger = LoggerFactory.getLogger(BrokerService.class);

  /**
   * Instantiates a new Broker service.
   *
   * @param stockChangePriceEventBlockingQueue the stock change price event blocking queue to be listened for stock price change event
   * @param stockMarket                        the stock market to be updated
   * @param portfolioConsole                   the portfolio console for printing
   * @param displayBlockingLock                the display blocking lock to wait for stock price change complete
   */
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
    this.stockPriceUpdateConsumerRunnables = new ArrayList<>();
  }

  /**
   * Register portfolio
   *
   * @param portfolio the portfolio
   */
  public void registerPortfolio(Portfolio portfolio){
    logger.debug("Register New Portfolio: {}", portfolio.getName());
    subscribers.add(portfolio);
    portfolioConsole.registerPortfolio(portfolio);
  }

  /**
   * Start consumers
   */
  @PostConstruct
  public void startConsumers(){
    ExecutorService consumerPool = Executors.newFixedThreadPool(NUM_CONSUMERS);

    for (int i = 1; i <= NUM_CONSUMERS; i++) {
      StockPriceUpdateConsumerRunnable consstockPriceUpdateConsumerRunnable = new StockPriceUpdateConsumerRunnable(
              stockChangePriceEventBlockingQueue, stockMarket, subscribers, "Consumer-" + i, displayBlockingLock);
      stockPriceUpdateConsumerRunnables.add(consstockPriceUpdateConsumerRunnable);
      consumerPool.submit(consstockPriceUpdateConsumerRunnable);
    }
  }

  /**
   * Stop all consumers
   */
  @PreDestroy
  public void stopConsumers(){
    for(StockPriceUpdateConsumerRunnable consumer: stockPriceUpdateConsumerRunnables){
      consumer.stop();
    }
  }
}
