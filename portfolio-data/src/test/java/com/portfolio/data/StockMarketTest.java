package com.portfolio.data;

import com.portfolio.dto.dict.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockMarketTest {

  private Map<String, Stock> stockDictionary;
  private ConcurrentHashMap<String, BigDecimal> stockPriceCache;
  private StockMarket target;

  @BeforeEach
  void beforeEach(){
    stockDictionary = new HashMap<>();
    stockDictionary.put("AAPL", new Stock(1000, 0.5, 0.5));
    stockDictionary.put("TELSA", new Stock(500, 0.5, 0.5));
    stockPriceCache = new ConcurrentHashMap<>();
    target = new StockMarket(stockDictionary, stockPriceCache);
  }

  @Test
  void setupCache(){

    target = new StockMarket(stockDictionary, stockPriceCache);

    assertEquals(2, stockPriceCache.size());
    assertEquals(BigDecimal.valueOf(1000.0), stockPriceCache.get("AAPL"));
    assertEquals(BigDecimal.valueOf(500.0), stockPriceCache.get("TELSA"));
  }

  @Test
  void getStockDictionary(){
    Map<String, Stock> actual = target.getStockDictionary();

    assertEquals(stockDictionary, actual);
  }

  @Test
  void getStockPriceMap(){
    Map<String, BigDecimal> actual = target.getStockPriceMap();

    assertEquals(BigDecimal.valueOf(1000.0),actual.get("AAPL"));
    assertEquals(BigDecimal.valueOf(500.0),actual.get("TELSA"));
  }

  @Test
  void upsertStockPrice(){
    String mockSymbol = "AAPL";

    ArgumentCaptor<String> symbolArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<BigDecimal> priceArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);

    ConcurrentHashMap<String, BigDecimal> spyCache =  spy(new ConcurrentHashMap<>());
    target = new StockMarket(stockDictionary, spyCache);

    target.upsertStockPrice(mockSymbol, BigDecimal.ONE);

    assertEquals(mockSymbol, symbolArgumentCaptor.getAllValues().get(symbolArgumentCaptor.getAllValues().size()-1));
    assertEquals(BigDecimal.ONE, priceArgumentCaptor.getAllValues().get(priceArgumentCaptor.getAllValues().size()-1));

  }
}