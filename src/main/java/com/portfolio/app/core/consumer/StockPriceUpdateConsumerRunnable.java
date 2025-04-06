package com.portfolio.app.core.consumer;

import com.portfolio.data.Portfolio;
import com.portfolio.data.StockMarket;
import com.portfolio.dto.event.StockChangePriceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * The type Stock price update consumer runnable.
 * Is a consumer abstraction to poll a Stock Price Change Event, process update to Stock Market and broadcast to subscribers
 * This consumer runnable can be run in parallel by threads
 */
public class StockPriceUpdateConsumerRunnable implements Runnable{

  private final BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue;
  private final StockMarket stockMarket;
  private final List<Portfolio> subscribers;
  private volatile  boolean running;
  private final String consumerName;
  private final Object displayBlockingLock;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceUpdateConsumerRunnable.class);

  /**
   * Instantiates a new Stock price update consumer runnable.
   *
   * @param stockChangePriceEventBlockingQueue the stock change price event blocking queue
   * @param stockMarket                        the stock market
   * @param subscribers                        the subscribers
   * @param consumerName                       the consumer name
   * @param displayBlockingLock                the display blocking lock
   */
  public StockPriceUpdateConsumerRunnable(
    BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue,
    StockMarket stockMarket,
    List<Portfolio> subscribers,
    String consumerName,
    Object displayBlockingLock
  ){
    this.stockChangePriceEventBlockingQueue = stockChangePriceEventBlockingQueue;
    this.stockMarket = stockMarket;
    this.subscribers = subscribers;
    this.consumerName = consumerName;
    this.displayBlockingLock = displayBlockingLock;
  }

  @Override
  public void run() {
    try {
      logger.debug("Stock Price Update Consumer:{} starts", consumerName);
      running = true;
      while (running) {
        // Consume Stock Change Event and Update Stock Market
        StockChangePriceEvent event = stockChangePriceEventBlockingQueue.take();
        stockMarket.upsertStockPrice(event.getSymbol(), event.getNewPrice());

        // Broadcast to subscribers to update their portfolio
        synchronized (displayBlockingLock) {
          subscribers.forEach(subscriber -> subscriber
                  .updateBySymbolOrUnderlyingSymbolPrice(event.getSymbol(), event.getNewPrice()));
          // notify PortfolioConsole to wake up and print updated portfolios
          if(event.isFinalEvent()){
            displayBlockingLock.notifyAll();
          }
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    logger.debug("Stock Price Update Consumer:{} has stopped", consumerName);
  }

  /**
   * Stop.
   */
  public void stop(){
    running = false;
  }

}
