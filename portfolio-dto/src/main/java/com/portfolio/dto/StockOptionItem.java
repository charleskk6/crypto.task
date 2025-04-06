package com.portfolio.dto;

import com.portfolio.algor.EuropeanOptionPricing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * The type Stock option item.
 */
public class StockOptionItem implements IAssetInterface {
  private String symbol;
  private String underlyingSymbol;
  private StockOptionType type;
  private BigDecimal strikePrice;
  private LocalDate expirationDate;
  private int size;

  /**
   * Instantiates a new Stock option item.
   */
  public StockOptionItem(){}

  /**
   * Instantiates a new Stock option item.
   *
   * @param symbol           the symbol
   * @param underlyingSymbol the underlying symbol
   * @param type             the type
   * @param strikePrice      the strike price
   * @param expirationDate   the expiration date
   * @param size             the size
   */
  public StockOptionItem(String symbol, String underlyingSymbol, StockOptionType type, BigDecimal strikePrice, LocalDate expirationDate, int size) {
    this.symbol = symbol;
    this.underlyingSymbol = underlyingSymbol;
    this.type = type;
    this.strikePrice = strikePrice;
    this.expirationDate = expirationDate;
    this.size = size;
  }

  private StockOptionItem(Builder builder) {
    this.symbol = builder.symbol;
    this.underlyingSymbol = builder.underlyingSymbol;
    this.strikePrice = builder.strikePrice;
    this.expirationDate = builder.expirationDate;
    this.type = builder.type;
    this.size = builder.size;
  }

  @Override
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
   * Gets underlying symbol.
   *
   * @return the underlying symbol
   */
  public String getUnderlyingSymbol() {
    return underlyingSymbol;
  }

  /**
   * Sets underlying symbol.
   *
   * @param underlyingSymbol the underlying symbol
   */
  public void setUnderlyingSymbol(String underlyingSymbol) {
    this.underlyingSymbol = underlyingSymbol;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public StockOptionType getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(StockOptionType type) {
    this.type = type;
  }

  /**
   * Gets strike price.
   *
   * @return the strike price
   */
  public BigDecimal getStrikePrice() {
    return strikePrice;
  }

  /**
   * Sets strike price.
   *
   * @param strikePrice the strike price
   */
  public void setStrikePrice(BigDecimal strikePrice) {
    this.strikePrice = strikePrice;
  }

  /**
   * Gets expiration date.
   *
   * @return the expiration date
   */
  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  /**
   * Sets expiration date.
   *
   * @param expirationDate the expiration date
   */
  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

  @Override
  public int getSize() {
    return size;
  }

  /**
   * Sets size.
   *
   * @param size the size
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * This method set new price of Call and Put StockOption by EuropeanOptionPricing model
   * @param priceOrUnderlyingPrice the new price the underlying Stock
   * @return the price in BigDecimal
   */
  @Override
  public BigDecimal calculatePrice(BigDecimal priceOrUnderlyingPrice) {
    long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    // Convert days to years (considering leap years)

    double yearsBetween = daysBetween / 365.25;
    double newPrice;
    if(StockOptionType.PUT.equals(this.type)){
      newPrice = EuropeanOptionPricing.putPrice(
        priceOrUnderlyingPrice.doubleValue(), strikePrice.doubleValue(), yearsBetween);
    } else {
      newPrice = EuropeanOptionPricing.callPrice(
        priceOrUnderlyingPrice.doubleValue(), strikePrice.doubleValue(), yearsBetween);
    }

    return BigDecimal.valueOf(newPrice);
  }

  /**
   * Builder builder.
   *
   * @return the builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The type Builder.
   */
// Builder class for StockOptionItem
  public static class Builder {
    private String symbol;
    private String underlyingSymbol;
    private StockOptionType type;
    private BigDecimal strikePrice;
    private LocalDate expirationDate;
    private int size;

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
     * Underlying symbol builder.
     *
     * @param underlyingSymbol the underlying symbol
     * @return the builder
     */
    public Builder underlyingSymbol(String underlyingSymbol) {
      this.underlyingSymbol = underlyingSymbol;
      return this;
    }

    /**
     * Type builder.
     *
     * @param type the type
     * @return the builder
     */
    public Builder type(StockOptionType type) {
      this.type = type;
      return this;
    }

    /**
     * Strike price builder.
     *
     * @param strikePrice the strike price
     * @return the builder
     */
    public Builder strikePrice(BigDecimal strikePrice){
      this.strikePrice = strikePrice;
      return this;
    }

    /**
     * Expiration date builder.
     *
     * @param expirationDate the expiration date
     * @return the builder
     */
    public Builder expirationDate(LocalDate expirationDate){
      this.expirationDate = expirationDate;
      return this;
    }

    /**
     * Size builder.
     *
     * @param size the size
     * @return the builder
     */
    public Builder size(int size) {
      this.size = size;
      return this;
    }

    /**
     * Build stock option item.
     *
     * @return the stock option item
     */
    public StockOptionItem build() {
      return new StockOptionItem(this);
    }
  }
}
