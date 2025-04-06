package com.portfolio.app.core.consumer;

import com.portfolio.data.StockPriceCache;
import com.portfolio.data.UserProfile;
import com.portfolio.dto.event.StockPriceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class StockPriceUpdateConsumer implements Runnable{

  private final BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue;
  private final StockPriceCache stockPriceCache;
  private final List<UserProfile> subscribers;
  private volatile  boolean running;
  private final String consumerName;
  private final Object displayBlockingLock;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceUpdateConsumer.class);

  public StockPriceUpdateConsumer(
    BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue,
    StockPriceCache stockPriceCache,
    List<UserProfile> subscribers,
    String consumerName,
    Object displayBlockingLock
  ){
    this.stockPriceEventBlockingQueue = stockPriceEventBlockingQueue;
    this.stockPriceCache = stockPriceCache;
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
        StockPriceEvent event = stockPriceEventBlockingQueue.take();
        stockPriceCache.upsertStockPrice(event.getSymbol(), event.getNewPrice());

        synchronized (displayBlockingLock) {
          subscribers.forEach(subscriber -> subscriber.updatePortfolio(event.getSymbol(), event.getNewPrice()));
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

  public void stop(){
    running = false;
  }

}
