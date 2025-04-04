package com.portfolio.dto;

import java.math.BigDecimal;

public class AssetWrapper<T extends IAssetInterface> {
  private final T asset;
  private BigDecimal price;
  private BigDecimal value;

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

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }
}
