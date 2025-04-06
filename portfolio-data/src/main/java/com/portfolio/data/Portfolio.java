package com.portfolio.data;

import com.portfolio.dto.AssetWrapper;
import com.portfolio.dto.IAssetInterface;
import com.portfolio.dto.dict.Stock;
import com.portfolio.factory.AssetWrapperFactory;
import com.portfolio.util.MonthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Portfolio is a DataType created by paring a csv file.
 */
public class Portfolio {

  private final String name;

  private final ConcurrentHashMap<String, AssetWrapper<? extends IAssetInterface>> portfolioCache;

  private final String positionFilePath;

  private static final Logger logger = LoggerFactory.getLogger(Portfolio.class);

  /**
   * Instantiates a new Portfolio.
   *
   * @param name             the portfolio name
   * @param positionFilePath the position file path
   * @param portfolioCache   Nullable portfolioCache
   */
  public Portfolio(
    final String name, final String positionFilePath,
    @Nullable final ConcurrentHashMap<String, AssetWrapper<? extends IAssetInterface>> portfolioCache
  ){
    this.name = name;
    this.positionFilePath = positionFilePath;
    this.portfolioCache = Optional.ofNullable(portfolioCache).orElse(new ConcurrentHashMap<>());
  }

  /**
   * Setup Portfolio with StockMarket which captured current price of Stocks
   *
   * @param stockMarket    stock market containing stock prices
   */
  public void setupPortfolio(StockMarket stockMarket){
    try (InputStream inputStream = Portfolio.class.getResourceAsStream(positionFilePath)) {

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

          final String symbol = values[0];
          final String positionSize = values[1];
          final String symbolOrUnderlyingSymbol = symbol.split("-")[0];
          final Stock underlying = stockMarket.getStockDictionary().get(symbolOrUnderlyingSymbol);
          if(Objects.nonNull(underlying)){
            portfolioCache.put(symbol, AssetWrapperFactory.create(
                    symbol, Integer.parseInt(positionSize), BigDecimal.valueOf(underlying.getInitialPrice())));
          }
        }
      }
    } catch (IOException e) {
      logger.error("Initial Position error",e);
    }
  }

  /**
   * Get name string.
   *
   * @return the string
   */
  public String getName(){
    return name;
  }

  /**
   * Get assets in portfolio and sort asset by Symbol orders
   *
   * @return the list
   */
  public List<AssetWrapper<? extends IAssetInterface>> getAssets(){
    PriorityQueue<AssetWrapper<? extends IAssetInterface>> pq =
            new PriorityQueue<>((a1, a2) -> sort(a1.getSymbol(), a2.getSymbol()));
    for(Map.Entry<String, AssetWrapper<? extends IAssetInterface>> entry: portfolioCache.entrySet()){
      pq.add(entry.getValue());
    }

    List<AssetWrapper<? extends IAssetInterface>> assets = new ArrayList<>();
    while(!pq.isEmpty()){
      assets.add(pq.poll());
    }
    return assets;
  }

  private int sort(final String symbol1,final String symbol2){
    String[] arguments1 = symbol1.split("-");
    String[] arguments2 = symbol2.split("-");

    if(symbol1.contains("-") && symbol2.contains("-")){ // Sort between Options
      String underlying1 = arguments1[0];
      String underlying2 = arguments2[0];

      if(underlying1.equals(underlying2)){ // Sort with Call and Put Option with same underlying
        int year1 = Integer.parseInt(arguments1[2]);
        int month1 = MonthUtil.MONTH_MAP.get(arguments1[1]);
        int year2 = Integer.parseInt(arguments2[2]);
        int month2 = MonthUtil.MONTH_MAP.get(arguments2[1]);
        YearMonth yearMonth1 = YearMonth.of(year1, month1);
        YearMonth yearMonth2 = YearMonth.of(year2, month2);
        String type1 = arguments1[4];
        String type2 = arguments2[4];

        if(yearMonth1.isBefore(yearMonth2)){ // Sort by Expiry Date
          return -1;
        } else if (yearMonth1.isAfter(yearMonth2)) {
          return 1;
        } else if(type1.equals("C") && type2.equals("P")){
          return -1;
        } else {
          return 1;
        }
      } else { // Sort with Options with different underlying
        return underlying1.compareTo(underlying2);
      }
    } else { // Sort Stocks and Options by name
      return symbol1.compareTo(symbol2);
    }
  }

  /**
   * Update asset Price
   *
   * @param symbolOrUnderlyingSymbol the symbol or underlying symbol
   * @param priceOrUnderlyingPrice   the price or underlying price
   */
  public void updateBySymbolOrUnderlyingSymbolPrice(
          final String symbolOrUnderlyingSymbol, final BigDecimal priceOrUnderlyingPrice){
    logger.debug("updateBySymbolOrUnderlyingSymbolPrice: {}", symbolOrUnderlyingSymbol);
    portfolioCache.entrySet()
      .parallelStream()
      .filter(entry -> entry.getKey().startsWith(symbolOrUnderlyingSymbol))
      .forEach(entry -> entry.getValue().setPriceOrUnderlyingPrice(priceOrUnderlyingPrice));
    logger.debug("Update Price Completed");
  }

  /**
   * Get total value of the whole portfolio
   *
   * @return the big decimal
   */
  public BigDecimal getTotalValue(){
    BigDecimal total = BigDecimal.ZERO;
    for(AssetWrapper<? extends IAssetInterface> asset: portfolioCache.values()){
      if(Objects.nonNull(asset.getValue())){
        total = total.add(asset.getValue());
      }
    }
    return total;
  }
}
