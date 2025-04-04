package com.portfolio.app.core.consumer;

import com.portfolio.data.StockPriceCache;
import com.portfolio.dto.StockPriceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class StockPriceUpdateConsumer implements Runnable{

  private final BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue;
  private final StockPriceCache stockPriceCache;
  private volatile  boolean running;
  private final String consumerName;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceUpdateConsumer.class);

  public StockPriceUpdateConsumer(
    BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue,
    StockPriceCache stockPriceCache,
    String consumerName
  ){
    this.stockPriceEventBlockingQueue = stockPriceEventBlockingQueue;
    this.stockPriceCache = stockPriceCache;
    this.consumerName = consumerName;
  }

  @Override
  public void run() {
    try {
      logger.debug("Stock Price Update Consumer:{} starts", consumerName);
      running = true;
      while (running) {
        StockPriceEvent event = stockPriceEventBlockingQueue.take();
        stockPriceCache.upsertStockPrice(event.getSymbol(), event.getNewPrice());
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
