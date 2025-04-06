package com.portfolio.app.core.service;

import com.portfolio.data.Portfolio;

public interface IBrokerService {
  // Register Market Data Subscriber
  void registerPortfolio(Portfolio portfolio);
}
