package com.portfolio.dto;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Asset wrapper is the General Class of StockItem and StockOptionItem
 * While the parameter type StockItem and StockOptionItem contain only their properties e.g. symbol, qty, etc
 * This wrapper class is the place where define an asset price and value
 * @param <T> the type parameter
 */
public class AssetWrapper<T extends IAssetInterface> {
  private final T asset;
  private BigDecimal price;

  /**
   * Instantiates a new Asset wrapper.
   *
   * @param asset the asset
   */
  public AssetWrapper(T asset) {
    this.asset = asset;
  }

  /**
   * Get symbol string.
   *
   * @return the string
   */
  public String getSymbol(){
    return asset.getSymbol();
  }

  /**
   * Get size int.
   *
   * @return the int
   */
  public int getSize(){
    return asset.getSize();
  }

  /**
   * Gets price.
   *
   * @return the price
   */
  public BigDecimal getPrice() {
    return price;
  }

  /**
   * Sets price or underlying price.
   * For assets, e.g. StockOptionItem, has its own way to define its price from their underlying price
   * This setter method let the price being calculated in Concrete class method: calculatePrice,  by Inversion of Control
   * @param priceOrUnderlyingPrice the price or underlying price
   */
  public void setPriceOrUnderlyingPrice(BigDecimal priceOrUnderlyingPrice) {
    this.price = asset.calculatePrice(priceOrUnderlyingPrice);
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public BigDecimal getValue() {
    return Optional.ofNullable(price)
            .map(p -> p.multiply(BigDecimal.valueOf(getSize()))).orElse(null);
  }
}
