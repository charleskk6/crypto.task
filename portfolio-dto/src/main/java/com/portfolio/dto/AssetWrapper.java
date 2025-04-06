package com.portfolio.dto;

import java.math.BigDecimal;
import java.util.Optional;

public class AssetWrapper<T extends IAssetInterface> {
  private final T asset;
  private BigDecimal price;

  public AssetWrapper(T asset) {
    this.asset = asset;
  }

  public String getSymbol(){
    return asset.getSymbol();
  }

  public int getSize(){
    return asset.getSize();
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPriceOrUnderlyingPrice(BigDecimal priceOrUnderlyingPrice) {
    this.price = asset.calculatePrice(priceOrUnderlyingPrice);
  }

  public BigDecimal getValue() {
    return Optional.ofNullable(price)
            .map(p -> p.multiply(BigDecimal.valueOf(getSize()))).orElse(null);
  }
}
