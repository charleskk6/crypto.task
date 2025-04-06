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

/**
 * The type StockPriceChangeEventProducerRunnable is a runnable class which simulate Stock Price change events
 */
public class StockPriceChangeEventProducerRunnable implements Runnable{

  // Blocking queue for queuing up stock price change event
  private final BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue;
  // Retrieving current Price of stocks in Market
  private final StockMarket stockMarket;

  // Interval for trigger price change events
  private final Integer stockPriceSimulateDurationMin;
  private final Integer stockPriceSimulateDurationMax;

  // Keep daemon thread running
  private volatile boolean running;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceChangeEventProducerRunnable.class);

  /**
   * Instantiates a new Stock price change event producer runnable.
   *
   * @param stockChangePriceEventBlockingQueue the blocking queue for queuing up stock price change event
   * @param stockMarket                        the stock market with current price of stocks
   * @param stockPriceSimulateDurationMin      the stock price simulate duration min
   * @param stockPriceSimulateDurationMax      the stock price simulate duration max
   */
  public StockPriceChangeEventProducerRunnable(
          BlockingQueue<StockChangePriceEvent> stockChangePriceEventBlockingQueue,
          StockMarket stockMarket,
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
    // Count iteration of Price change of Stocks
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

    // Loop until there exist at least one price change event
    do {
      for(Map.Entry<String, BigDecimal> entry: stockMarket.getStockPriceMap().entrySet()){
        // Random Simulate price change of a stock
        if(random.nextBoolean()){
          String symbol = entry.getKey();
          BigDecimal currentPrice = entry.getValue();
          // Get Mu and Sigma
          final Stock stock = stockMarket.getStockDictionary().get(symbol);
          final double mu = stock.getMu();
          final double sigma = stock.getSigma();
          // Insert Event
          events.add(
            StockChangePriceEvent.builder()
              .symbol(symbol)
              .newPrice(
                BigDecimal.valueOf(
                  // Generate new Price
                  new GeometricBrownianMotion(mu, sigma)
                          .getNextPrice(currentPrice.doubleValue(), 60)
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

  /**
   * Stop.
   */
  public void stop(){
    running = false;
  }
}
