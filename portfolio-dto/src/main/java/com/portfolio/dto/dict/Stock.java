package com.portfolio.dto.dict;

public class Stock {
  private double initialPrice;
  private double mu;
  private double sigma;

  public Stock(){}

  public Stock(double initialPrice, double mu, double sigma) {
    this.initialPrice = initialPrice;
    this.mu = mu;
    this.sigma = sigma;
  }

  // Getters and setters
  public double getInitialPrice() {
    return initialPrice;
  }

  public void setInitialPrice(double initialPrice) {
    this.initialPrice = initialPrice;
  }

  public double getMu() {
    return mu;
  }

  public void setMu(double mu) {
    this.mu = mu;
  }

  public double getSigma() {
    return sigma;
  }

  public void setSigma(double sigma) {
    this.sigma = sigma;
  }
}