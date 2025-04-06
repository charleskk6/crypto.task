package com.portfolio.dto;

import java.math.BigDecimal;

/**
 * The interface Asset interface.
 */
public interface IAssetInterface {
  /**
   * Gets symbol.
   *
   * @return the symbol
   */
  String getSymbol();

  /**
   * Gets size.
   *
   * @return the size
   */
  int getSize();

  /**
   * Calculate price by concrete class method: calculatePrice, by inversion of control
   *
   * @param priceOrUnderlyingPrice the price or underlying price
   * @return the big decimal
   */
// Inversion of Control to Calculate Price defined by Concrete Class
  BigDecimal calculatePrice(BigDecimal priceOrUnderlyingPrice);
}
