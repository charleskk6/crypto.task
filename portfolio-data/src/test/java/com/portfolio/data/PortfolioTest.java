package com.portfolio.data;

import com.portfolio.dto.AssetWrapper;
import com.portfolio.dto.IAssetInterface;
import com.portfolio.dto.dict.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PortfolioTest {

  private String name;

  private ConcurrentHashMap<String, AssetWrapper<? extends IAssetInterface>> mockPortfolioCache;

  private String positionFilePath;

  private Portfolio target;

  @BeforeEach
  void beforeEach(){
    this.name = "name";
    this.positionFilePath = "/position.csv";

    mockPortfolioCache = new ConcurrentHashMap<>();
    target = new Portfolio(name, positionFilePath, mockPortfolioCache);
  }

  @Test
  void setupPortfolio_withFileExist(){
    StockMarket stockMarket = mock(StockMarket.class);

    Map<String, Stock> mockStockDict = new HashMap<>();
    mockStockDict.put("AAPL", new Stock(1000.00, 0.5, 0.5));

    doReturn(mockStockDict).when(stockMarket).getStockDictionary();
    target.setupPortfolio(stockMarket);

    assertEquals(1, mockPortfolioCache.size());
    assertEquals(BigDecimal.valueOf(1000.00), mockPortfolioCache.get("AAPL").getPrice());
  }

  @Test
  void setupPortfolio_withFileNotExist(){
    StockMarket stockMarket = mock(StockMarket.class);

    target = new Portfolio(name, "./Invalid_path.csv", mockPortfolioCache);
    target.setupPortfolio(stockMarket);

    assertEquals(0, mockPortfolioCache.size());
  }

  @Test
  void getName(){
    assertEquals(this.name, target.getName());
  }

  @Test
  void getAsset(){
    StockMarket stockMarket = mock(StockMarket.class);

    Map<String, Stock> mockStockDict = new HashMap<>();
    mockStockDict.put("AAPL", new Stock(1000.00, 0.5, 0.5));
    mockStockDict.put("TELSA", new Stock(2000.00, 0.5, 0.5));

    doReturn(mockStockDict).when(stockMarket).getStockDictionary();
    target.setupPortfolio(stockMarket);
    List<AssetWrapper<? extends IAssetInterface>> assets = target.getAssets();

    assertEquals(2, mockPortfolioCache.size());
  }

  @Test
  void updateBySymbolOrUnderlyingSymbolPrice(){
    StockMarket stockMarket = mock(StockMarket.class);

    Map<String, Stock> mockStockDict = new HashMap<>();
    mockStockDict.put("AAPL", new Stock(1000.00, 0.5, 0.5));

    doReturn(mockStockDict).when(stockMarket).getStockDictionary();
    target.setupPortfolio(stockMarket);

    AssetWrapper<? extends IAssetInterface> mockAsset = spy(mockPortfolioCache.get("AAPL"));
    mockPortfolioCache.put("AAPL", mockAsset);

    target.updateBySymbolOrUnderlyingSymbolPrice("AAPL", BigDecimal.valueOf(500.00));

    ArgumentCaptor<BigDecimal> updatePriceArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
    verify(mockAsset, times(1)).setPriceOrUnderlyingPrice(updatePriceArgumentCaptor.capture());
    assertEquals(BigDecimal.valueOf(500.0), updatePriceArgumentCaptor.getValue());
  }

  @Test
  void getTotalValue(){
    StockMarket stockMarket = mock(StockMarket.class);

    Map<String, Stock> mockStockDict = new HashMap<>();
    mockStockDict.put("AAPL", new Stock(1000.00, 0.5, 0.5));
    mockStockDict.put("TELSA", new Stock(2000.00, 0.5, 0.5));

    doReturn(mockStockDict).when(stockMarket).getStockDictionary();
    target.setupPortfolio(stockMarket);
    BigDecimal actual = target.getTotalValue();

    assertEquals(BigDecimal.valueOf(2000000.0), actual);
  }
}