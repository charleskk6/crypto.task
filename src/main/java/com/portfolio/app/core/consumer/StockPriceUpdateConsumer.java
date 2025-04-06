package com.portfolio.app.core.consumer;

import com.portfolio.data.Portfolio;
import com.portfolio.data.StockMarket;
import com.portfolio.dto.event.StockChangePriceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class StockPriceUpdateConsumer implements Runnable{

  private final BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue;
  private final StockMarket stockMarket;
  private final List<Portfolio> subscribers;
  private volatile  boolean running;
  private final String consumerName;
  private final Object displayBlockingLock;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceUpdateConsumer.class);

  public StockPriceUpdateConsumer(
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
        StockChangePriceEvent event = stockChangePriceEventBlockingQueue.take();
        stockMarket.upsertStockPrice(event.getSymbol(), event.getNewPrice());

        synchronized (displayBlockingLock) {
          subscribers.forEach(subscriber -> subscriber
                  .updateBySymbolOrUnderlyingSymbolPrice(event.getSymbol(), event.getNewPrice()));
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
