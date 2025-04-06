package com.portfolio.dto.dict;

/**
 * Stock is DataType abstract Stock Configuration defined in application properties file
 */
public class Stock {
  private double initialPrice;
  private double mu;
  private double sigma;

  /**
   * Instantiates a new Stock.
   */
  public Stock(){}

  /**
   * Instantiates a new Stock.
   *
   * @param initialPrice the initial price
   * @param mu           the mu
   * @param sigma        the sigma
   */
  public Stock(double initialPrice, double mu, double sigma) {
    this.initialPrice = initialPrice;
    this.mu = mu;
    this.sigma = sigma;
  }

  /**
   * Gets initial price.
   *
   * @return the initial price
   */
// Getters and setters
  public double getInitialPrice() {
    return initialPrice;
  }

  /**
   * Sets initial price.
   *
   * @param initialPrice the initial price
   */
  public void setInitialPrice(double initialPrice) {
    this.initialPrice = initialPrice;
  }

  /**
   * Gets mu.
   *
   * @return the mu
   */
  public double getMu() {
    return mu;
  }

  /**
   * Sets mu.
   *
   * @param mu the mu
   */
  public void setMu(double mu) {
    this.mu = mu;
  }

  /**
   * Gets sigma.
   *
   * @return the sigma
   */
  public double getSigma() {
    return sigma;
  }

  /**
   * Sets sigma.
   *
   * @param sigma the sigma
   */
  public void setSigma(double sigma) {
    this.sigma = sigma;
  }
}