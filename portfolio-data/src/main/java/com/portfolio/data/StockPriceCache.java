package com.portfolio.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StockPriceCache {

  private final ConcurrentHashMap<String, BigDecimal> stockPriceCache;

  private static final String POSITION_FILE_PATH = "/position.csv";

  private final Random random = new Random();

  private final Integer stockPriceLowerBound;
  private final Integer stockPriceUpperBound;

  private static final Logger logger = LoggerFactory.getLogger(StockPriceCache.class);

  public StockPriceCache(
    final Integer stockPriceLowerBound,
    final Integer stockPriceUpperBound
  ){
    this.stockPriceLowerBound = stockPriceLowerBound;
    this.stockPriceUpperBound = stockPriceUpperBound;
    this.stockPriceCache = new ConcurrentHashMap<>();
    setupCache();
  }

  private void setupCache(){
    try (InputStream inputStream = StockPriceCache.class.getResourceAsStream(POSITION_FILE_PATH)) {

      // Ensure the file exists
      if (inputStream == null) {
        logger.error("File not found in resources.");
        return;
      }

      try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        //Skip header line
        br.readLine();

        while ((line = br.readLine()) != null) {
          String[] values = line.split(",");

          String symbol = values[0];
          String[] notations = symbol.split("-");
          if(notations.length == 1){
            stockPriceCache.put(symbol, generateInitialPrice());
          }
        }
      }
    } catch (IOException e) {
      logger.error("Initial StockPrice error",e);
    }
  }

  public Optional<BigDecimal> getStockPrice(final String symbol){
    return Optional.ofNullable(stockPriceCache.get(symbol));
  }

  public Set<Map.Entry<String, BigDecimal>> getStockPriceEntrySets(){
    return stockPriceCache.entrySet();
  }

  public void upsertStockPrice(final String symbol, BigDecimal price){
    logger.info("{} change to {}", symbol, String.format("%.2f", price));
    stockPriceCache.put(symbol, price);
  }

  private BigDecimal generateInitialPrice(){
    double randomDouble = stockPriceLowerBound + ((stockPriceUpperBound-1) * random.nextDouble());
    return BigDecimal.valueOf(randomDouble).setScale(2, RoundingMode.HALF_UP);
  }
}
