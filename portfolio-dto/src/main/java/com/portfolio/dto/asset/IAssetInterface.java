package com.portfolio.dto.asset;

import java.math.BigDecimal;

public interface IAssetInterface {
  String getSymbol();
  int getSize();

  // Inversion of Control to Calculate Price defined by Concrete Class
  BigDecimal calculatePrice(BigDecimal price);
}
