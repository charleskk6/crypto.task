package com.portfolio.app.core.producer;

import com.portfolio.app.util.NewStockPriceGenerator;
import com.portfolio.data.StockPriceCache;
import com.portfolio.dto.StockPriceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class StockPriceChangeEventProducer implements Runnable{

  private final BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue;
  private final StockPriceCache stockPriceCache;
  private final Integer stockPriceSimulateDurationMin;
  private final Integer stockPriceSimulateDurationMax;
  private volatile boolean running;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceChangeEventProducer.class);

  public StockPriceChangeEventProducer(
    BlockingQueue<StockPriceEvent> stockPriceEventBlockingQueue, StockPriceCache stockPriceCache,
    Integer stockPriceSimulateDurationMin, Integer stockPriceSimulateDurationMax
  ){
    this.stockPriceEventBlockingQueue = stockPriceEventBlockingQueue;
    this.stockPriceCache = stockPriceCache;
    this.stockPriceSimulateDurationMin = stockPriceSimulateDurationMin;
    this.stockPriceSimulateDurationMax = stockPriceSimulateDurationMax;
  }

  @Override
  public void run() {
    logger.debug("Stock Price Simulator starts");
    final Random random = new Random();
    int iteration = 1;
    running = true;
    while (running) {
      try {
        List<StockPriceEvent> events = randomCreateStockPriceChangeEvents(stockPriceCache);
        if(events.size()>0){
          logger.info("## {} Market Data Update", iteration++);

          for(StockPriceEvent event: events){
            stockPriceEventBlockingQueue.put(event);
          }
        }

        // Sleep for the generated time
        int sleepTime = stockPriceSimulateDurationMin +
                random.nextInt(stockPriceSimulateDurationMax - stockPriceSimulateDurationMin + 1);
        Thread.sleep(sleepTime);

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Respect interruption
        break;
      }
    }
    logger.debug("Stock Price Simulator has Stopped");
  }

  private List<StockPriceEvent> randomCreateStockPriceChangeEvents(StockPriceCache stockPriceCache){
    List<StockPriceEvent> events = new ArrayList<>();
    Random random = new Random();
    for(Map.Entry<String, BigDecimal> entry: stockPriceCache.getStockPriceEntrySets()){
      if(random.nextBoolean()){
        String symbol = entry.getKey();
        BigDecimal price = entry.getValue();
        events.add(
          StockPriceEvent.builder()
            .symbol(symbol)
            .newPrice(NewStockPriceGenerator.generate(price))
            .build());
      }
    }
    return events;
  }

  public void stop(){
    running = false;
  }
}
