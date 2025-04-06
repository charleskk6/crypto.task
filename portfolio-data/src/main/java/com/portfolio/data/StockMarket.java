package com.portfolio.data;

import com.portfolio.dto.dict.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The StockMarket is the DataType, holding Current Stock Price and Stock Dictionary in the Market
 */
public class StockMarket {

  private final ConcurrentHashMap<String, BigDecimal> stockPriceCache;

  private static final String POSITION_FILE_PATH = "/position.csv";

  // Initial Price Map defined in properties file
  private final Map<String, Stock> stockDictionary;
  // Generate random price when initial stock price has not been defined ni properties
  private static final Random priceRandom = new Random();
  private static final Random muRandom = new Random();
  private static final Random sigmaRandom = new Random();
  private static final int MOCK_PRICE_LOWER_BOUND = 10;
  private static final int MOCK_PRICE_UPPER_BOUND = 500;

  // Logger
  private static final Logger logger = LoggerFactory.getLogger(StockMarket.class);

  /**
   * Instantiates a new StockMarket with Stock Dictionary configured in properties file
   *
   * @param stockDictionary the stock dictionary
   * @param stockPriceCache stockPriceCache for test case injection
   */
  public StockMarket(Map<String, Stock> stockDictionary, ConcurrentHashMap<String, BigDecimal> stockPriceCache){
    this.stockPriceCache = Optional.ofNullable(stockPriceCache).orElse(new ConcurrentHashMap<>());
    this.stockDictionary = stockDictionary;
    setupCache();
  }

  // Extract only Stock defined in position.csv, mapping with initial price setting in properties file
  private void setupCache(){
    try (InputStream inputStream = StockMarket.class.getResourceAsStream(POSITION_FILE_PATH)) {
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
            stockPriceCache.put(symbol, initialPrice(symbol));
          }
        }
      }
    } catch (IOException e) {
      logger.error("Initial StockPrice error",e);
    }
  }

  /**
   * Get stock dictionary
   *
   * @return the map
   */
  public Map<String, Stock> getStockDictionary(){
    return stockDictionary;
  }

  /**
   * Get stock price map
   *
   * @return the map
   */
  public Map<String, BigDecimal> getStockPriceMap(){
    return Collections.unmodifiableMap(new HashMap<>(stockPriceCache));
  }

  /**
   * Upsert stock price.
   *
   * @param symbol the symbol
   * @param price  the price
   */
  public void upsertStockPrice(final String symbol, BigDecimal price){
    stockPriceCache.put(symbol, price);
  }

  private BigDecimal initialPrice(final String symbol){
    // Create StockDict if symbol not found in config
    stockDictionary.putIfAbsent(symbol, new Stock(
            MOCK_PRICE_LOWER_BOUND + priceRandom.nextInt(MOCK_PRICE_UPPER_BOUND - MOCK_PRICE_LOWER_BOUND + 1),
            muRandom.nextDouble(), sigmaRandom.nextDouble()));

    Stock stock = stockDictionary.get(symbol);
    return BigDecimal.valueOf(stock.getInitialPrice());
  }
}
