package com.portfolio.app.core.service;

import com.portfolio.data.UserProfile;

public interface IBrokerService {
  // Register Market Data Subscriber
  void registerUserProfile(UserProfile userProfile);
}
