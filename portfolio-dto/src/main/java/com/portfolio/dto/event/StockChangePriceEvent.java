package com.portfolio.dto.event;

import java.math.BigDecimal;

/**
 * The type Stock change price event.
 */
public class StockChangePriceEvent {
  private String symbol;
  private BigDecimal newPrice;
  private boolean isFinalEvent;

  private StockChangePriceEvent(Builder builder) {
    this.symbol = builder.symbol;
    this.newPrice = builder.newPrice;
    this.isFinalEvent = builder.isFinalEvent;
  }

  /**
   * Gets symbol.
   *
   * @return the symbol
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Sets symbol.
   *
   * @param symbol the symbol
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  /**
   * Gets new price.
   *
   * @return the new price
   */
  public BigDecimal getNewPrice() {
    return newPrice;
  }

  /**
   * Sets new price.
   *
   * @param newPrice the new price
   */
  public void setNewPrice(BigDecimal newPrice) {
    this.newPrice = newPrice;
  }

  /**
   * Is final event boolean.
   *
   * @return the boolean
   */
  public boolean isFinalEvent() {
    return isFinalEvent;
  }

  /**
   * Sets final event.
   *
   * @param finalEvent the final event
   */
  public void setFinalEvent(boolean finalEvent) {
    isFinalEvent = finalEvent;
  }

  /**
   * Builder stock change price event . builder.
   *
   * @return the stock change price event . builder
   */
  public static StockChangePriceEvent.Builder builder() {
    return new StockChangePriceEvent.Builder();
  }

  /**
   * The type Builder.
   */
// Static Builder class
  public static class Builder {
    private String symbol;
    private BigDecimal newPrice;
    private boolean isFinalEvent;

    /**
     * Symbol builder.
     *
     * @param symbol the symbol
     * @return the builder
     */
    public Builder symbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    /**
     * New price builder.
     *
     * @param newPrice the new price
     * @return the builder
     */
    public Builder newPrice(BigDecimal newPrice) {
      this.newPrice = newPrice;
      return this;
    }

    /**
     * Is final event builder.
     *
     * @param isFinalEvent the is final event
     * @return the builder
     */
    public Builder isFinalEvent(boolean isFinalEvent){
      this.isFinalEvent = isFinalEvent;
      return this;
    }

    /**
     * Build stock change price event.
     *
     * @return the stock change price event
     */
    public StockChangePriceEvent build() {
      return new StockChangePriceEvent(this);
    }
  }
}
