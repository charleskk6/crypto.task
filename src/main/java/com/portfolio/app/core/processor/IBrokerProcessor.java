package com.portfolio.app.core.processor;

import com.portfolio.data.UserProfile;

public interface IBrokerProcessor {
  // Register Market Data Subscriber
  void registerUserProfile(UserProfile userProfile);
}
