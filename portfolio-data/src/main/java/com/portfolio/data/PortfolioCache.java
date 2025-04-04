package com.portfolio.data;

import com.portfolio.factory.AssetWrapperFactory;
import com.portfolio.dto.AssetWrapper;
import com.portfolio.dto.IAssetInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PortfolioCache {

  private final ConcurrentHashMap<String, AssetWrapper<? extends IAssetInterface>> portfolioCache;
  private final StockPriceCache stockPriceCache;

  private static final String POSITION_FILE_PATH = "/position.csv";

  private static final Logger logger = LoggerFactory.getLogger(PortfolioCache.class);

  public PortfolioCache(StockPriceCache stockPriceCache){
    this.stockPriceCache = stockPriceCache;
    this.portfolioCache = new ConcurrentHashMap<>();
    setupCache();
  }

  private void setupCache(){
    try (InputStream inputStream = PortfolioCache.class.getResourceAsStream(POSITION_FILE_PATH)) {

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

          stockPriceCache.getStockPrice(symbolOrUnderlyingSymbol)
            .ifPresent(price -> {
              portfolioCache.put(symbol, AssetWrapperFactory.create(symbol, Integer.parseInt(positionSize), price));
            });
        }
      }
    } catch (IOException e) {
      logger.error("Initial Position error",e);
    }
  }

  public Optional<AssetWrapper<? extends IAssetInterface>> getAsset(final String symbol){
    return Optional.ofNullable(portfolioCache.get(symbol));
  }

  public Set<Map.Entry<String, AssetWrapper<? extends  IAssetInterface>>> getPortfolioEntrySets(){
    return portfolioCache.entrySet();
  }

  public void upsertAsset(final String symbol, AssetWrapper<? extends IAssetInterface> asset){
    logger.debug("Update asset for symbol: {}", symbol);
    portfolioCache.put(symbol, asset);
  }



}
