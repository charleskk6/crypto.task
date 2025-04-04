package com.portfolio.dto;

import java.math.BigDecimal;

public class StockPriceEvent {
  private String symbol;
  private BigDecimal newPrice;

  private StockPriceEvent(Builder builder) {
    this.symbol = builder.symbol;
    this.newPrice = builder.newPrice;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public BigDecimal getNewPrice() {
    return newPrice;
  }

  public void setNewPrice(BigDecimal newPrice) {
    this.newPrice = newPrice;
  }

  public static StockPriceEvent.Builder builder() {
    return new StockPriceEvent.Builder();
  }

  // Static Builder class
  public static class Builder {
    private String symbol;
    private BigDecimal newPrice;

    public Builder symbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    public Builder newPrice(BigDecimal newPrice) {
      this.newPrice = newPrice;
      return this;
    }

    public StockPriceEvent build() {
      return new StockPriceEvent(this);
    }
  }
}
