package com.portfolio.algor;

import java.util.Random;

public class GeometricBrownianMotion {
  // Drift coefficient
  private final double mu;
  // Volatility coefficient
  private final double sigma;
  private final Random random;

  // Number of seconds in a trading year: 252 days * 8.5 hours/day * 3600 seconds/hour
  private static final double SECONDS_IN_TRADING_YEAR = 252 * 8.5 * 3600;

  public GeometricBrownianMotion(double mu, double sigma) {
    this.mu = mu;
    this.sigma = sigma;
    this.random = new Random();
  }

  /**
   * Simulate the next stock price after deltaT seconds
   *
   * @param currentPrice Current stock price S
   * @param deltaT       Time step in seconds (Δt)
   * @return New stock price S + ΔS
   */
  public double getNextPrice(double currentPrice, double deltaT) {
    double dt = deltaT / SECONDS_IN_TRADING_YEAR;
    double epsilon = random.nextGaussian(); // ~ N(0, 1)
    double deltaS = currentPrice * (mu * dt + sigma * epsilon * Math.sqrt(dt));
    return currentPrice + deltaS;
  }
}