package com.portfolio.data;

import com.portfolio.dto.asset.AssetWrapper;
import com.portfolio.dto.asset.IAssetInterface;
import com.portfolio.factory.AssetWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Portfolio {

  private final String name;

  private final ConcurrentHashMap<String, AssetWrapper<? extends IAssetInterface>> portfolioCache;

  private static final String POSITION_FILE_PATH = "/position.csv";

  private static final Logger logger = LoggerFactory.getLogger(Portfolio.class);

  public Portfolio(final String name){
    this.name = name;
    this.portfolioCache = new ConcurrentHashMap<>();
    setupCache();
  }

  private void setupCache(){
    try (InputStream inputStream = Portfolio.class.getResourceAsStream(POSITION_FILE_PATH)) {

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

          portfolioCache.put(symbol, AssetWrapperFactory.create(symbol, Integer.parseInt(positionSize), BigDecimal.ONE));
        }
      }
    } catch (IOException e) {
      logger.error("Initial Position error",e);
    }
  }

  public String getName(){
    return name;
  }

  public List<AssetWrapper<? extends IAssetInterface>> getAssets(){
    PriorityQueue<AssetWrapper<? extends IAssetInterface>> pq =
            new PriorityQueue<>(Comparator.comparing(AssetWrapper::getSymbol));
    for(Map.Entry<String, AssetWrapper<? extends IAssetInterface>> entry: portfolioCache.entrySet()){
      pq.add(entry.getValue());
    }

    List<AssetWrapper<? extends IAssetInterface>> assets = new ArrayList<>();
    while(!pq.isEmpty()){
      assets.add(pq.poll());
    }
    return assets;
  }

  public void updateBySymbolOrUnderlyingSymbolPrice(
          final String symbolOrUnderlyingSymbol, final BigDecimal priceOrUnderlyingPrice){
    logger.debug("updateBySymbolOrUnderlyingSymbolPrice: {}", symbolOrUnderlyingSymbol);
    portfolioCache.entrySet()
      .parallelStream()
      .filter(entry -> entry.getKey().startsWith(symbolOrUnderlyingSymbol))
      .forEach(entry -> {
        entry.getValue().setPriceOrUnderlyingPrice(priceOrUnderlyingPrice);
      });
    logger.debug("Update Price Completed");
  }

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
