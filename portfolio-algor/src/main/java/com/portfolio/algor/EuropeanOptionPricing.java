package com.portfolio.algor;

public class EuropeanOptionPricing {

  // Constants for the risk-free rate and volatility
  private static final double RISK_FREE_RATE = 0.02;  // 2%
  private static final double VOLATILITY = 0.2;      // 20%

  // Cumulative normal distribution function (standard normal CDF)
  public static double cumulativeNormalDistribution(double x) {
    return 0.5 * (1 + erf(x / Math.sqrt(2)));
  }

  // Error function approximation
  public static double erf(double z) {
    double sign = (z >= 0) ? 1 : -1;
    z = Math.abs(z);
    double t = 1 / (1 + 0.3275911 * z);
    double result = 1 - (((((1.061405429 * t - 1.453152027) * t + 1.421413741) * t - 0.284496736) * t + 0.254829592) * t * Math.exp(-z * z));
    return sign * result;
  }

  // Calculate d1 term in the Black-Scholes formula
  public static double d1(double S, double K, double t) {
    return (Math.log(S / K) + (RISK_FREE_RATE + 0.5 * VOLATILITY * VOLATILITY) * t) / (VOLATILITY * Math.sqrt(t));
  }

  // Calculate d2 term in the Black-Scholes formula
  public static double d2(double d1, double t) {
    return d1 - VOLATILITY * Math.sqrt(t);
  }

  // Calculate the price of a European call option
  public static double callPrice(double S, double K, double t) {
    double d1 = d1(S, K, t);
    double d2 = d2(d1, t);
    return S * cumulativeNormalDistribution(d1) - K * Math.exp(-RISK_FREE_RATE * t) * cumulativeNormalDistribution(d2);
  }

  // Calculate the price of a European put option
  public static double putPrice(double S, double K, double t) {
    double d1 = d1(S, K, t);
    double d2 = d2(d1, t);
    return K * Math.exp(-RISK_FREE_RATE * t) * cumulativeNormalDistribution(-d2) - S * cumulativeNormalDistribution(-d1);
  }
}
