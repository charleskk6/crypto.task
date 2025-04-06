package com.portfolio.data;

import java.math.BigDecimal;

public class UserProfile {

  private final Portfolio portfolio;

  public UserProfile(Portfolio portfolio){
    this.portfolio = portfolio;
  }

  public Portfolio getPortfolio(){
    return this.portfolio;
  }

  public void updatePortfolio(final String symbolOrUnderlyingSymbol, BigDecimal price){
    portfolio.updateBySymbolOrUnderlyingSymbolPrice(symbolOrUnderlyingSymbol, price);
  }

}
