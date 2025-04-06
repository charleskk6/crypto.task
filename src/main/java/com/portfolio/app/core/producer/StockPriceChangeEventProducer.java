package com.portfolio.app.core.producer;

import com.portfolio.algor.GeometricBrownianMotion;
import com.portfolio.data.StockMarket;
import com.portfolio.dto.dict.Stock;
import com.portfolio.dto.event.StockChangePriceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class StockPriceChangeEventProducer implements Runnable{

  private final BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue;
  private final StockMarket stockMarket;
  private final Integer stockPriceSimulateDurationMin;
  private final Integer stockPriceSimulateDurationMax;
  private volatile boolean running;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceChangeEventProducer.class);

  public StockPriceChangeEventProducer(
          BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue, StockMarket stockMarket,
          Integer stockPriceSimulateDurationMin, Integer stockPriceSimulateDurationMax
  ){
    this.stockChangePriceEventBlockingQueue = stockChangePriceEventBlockingQueue;
    this.stockMarket = stockMarket;
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
        // Sleep before the generated time
        int sleepTime = stockPriceSimulateDurationMin +
                random.nextInt(stockPriceSimulateDurationMax - stockPriceSimulateDurationMin + 1);
        Thread.sleep(sleepTime);

        List<StockChangePriceEvent> events = randomCreateStockPriceChangeEvents(stockMarket);
        logger.info("## {} Market Data Update", iteration++);
        for(StockChangePriceEvent event: events){
          logger.info("{} change to {}", event.getSymbol(), String.format("%,.2f", event.getNewPrice()));
          stockChangePriceEventBlockingQueue.put(event);
        }

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Respect interruption
        break;
      }
    }
    logger.debug("Stock Price Simulator has Stopped");
  }

  private List<StockChangePriceEvent> randomCreateStockPriceChangeEvents(StockMarket stockMarket){
    List<StockChangePriceEvent> events = new ArrayList<>();
    Random random = new Random();

    do {
      for(Map.Entry<String, BigDecimal> entry: stockMarket.getStockPriceMap().entrySet()){
        // Random Simulate price change of a stock
        if(random.nextBoolean()){
          String symbol = entry.getKey();
          BigDecimal price = entry.getValue();
          Stock stock = stockMarket.getStockDictionary().get(symbol);
          events.add(
            StockChangePriceEvent.builder()
              .symbol(symbol)
              .newPrice(
                      BigDecimal.valueOf(
                              // Generate new Price
                              new GeometricBrownianMotion(stock.getMu(),stock.getSigma()).getNextPrice(price.doubleValue(), 60)
                      )
              )
            .build());
        }
      }
    } while(events.isEmpty());

    // Sort event with Symbol name
    events.sort(Comparator.comparing(StockChangePriceEvent::getSymbol));
    // Final Event flag is use for signal Console to display new Portfolio
    events.get(events.size()-1).setFinalEvent(true);

    return events;
  }

  public void stop(){
    running = false;
  }
}
